package com.williamfiset.algorithms.datastructures.balancedtree;

import static com.google.common.truth.Truth.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import org.junit.Before;
import org.junit.Test;

public class AVLTreeTest {

  static final int MAX_RAND_NUM = +100000;
  static final int MIN_RAND_NUM = -100000;

  static final int TEST_SZ = 2500;
  static final int PRINT_SZ = 15;

  private AVLTreeRecursive<Integer> tree;

  @Before
  public void setup() {
    tree = new AVLTreeRecursive<>();
  }

  @Test
  public void testNullInsertion() {
    assertThat(tree.insert(null)).isFalse();
  }

  @Test
  public void testNullRemoval() {
    assertThat(tree.remove(null)).isFalse();
  }

  @Test
  public void testTreeContainsNull() {
    assertThat(tree.contains(null)).isFalse();
  }

  @Test
  public void testLeftLeftCase() {

    tree.insert(3);
    tree.insert(2);
    tree.insert(1);

    System.out.println(tree);

    assertThat(tree.root.value).isEqualTo(2);
    assertThat(tree.root.left.value).isEqualTo(1);
    assertThat(tree.root.right.value).isEqualTo(3);

    assertThat(tree.root.left.left).isNull();
    assertThat(tree.root.left.right).isNull();
    assertThat(tree.root.right.left).isNull();
    assertThat(tree.root.right.right).isNull();
  }

  @Test
  public void testLeftRightCase() {

    tree.insert(3);
    tree.insert(1);
    tree.insert(2);

    System.out.println(tree);

    assertThat(tree.root.value).isEqualTo(2);
    assertThat(tree.root.left.value).isEqualTo(1);
    assertThat(tree.root.right.value).isEqualTo(3);

    assertThat(tree.root.left.left).isNull();
    assertThat(tree.root.left.right).isNull();
    assertThat(tree.root.right.left).isNull();
    assertThat(tree.root.right.right).isNull();
  }

  @Test
  public void testRightRightCase() {

    tree.insert(1);
    tree.insert(2);
    tree.insert(3);

    System.out.println(tree);

    assertThat(tree.root.value).isEqualTo(2);
    assertThat(tree.root.left.value).isEqualTo(1);
    assertThat(tree.root.right.value).isEqualTo(3);

    assertThat(tree.root.left.left).isNull();
    assertThat(tree.root.left.right).isNull();
    assertThat(tree.root.right.left).isNull();
    assertThat(tree.root.right.right).isNull();
  }

  @Test
  public void testRightLeftCase() {

    tree.insert(1);
    tree.insert(3);
    tree.insert(2);

    System.out.println(tree);

    assertThat(tree.root.value).isEqualTo(2);
    assertThat(tree.root.left.value).isEqualTo(1);
    assertThat(tree.root.right.value).isEqualTo(3);

    assertThat(tree.root.left.left).isNull();
    assertThat(tree.root.left.right).isNull();
    assertThat(tree.root.right.left).isNull();
    assertThat(tree.root.right.right).isNull();
  }

  @Test
  public void testRandomizedBalanceFactorTest() {
    for (int i = 0; i < TEST_SZ; i++) {
      tree.insert(randValue());
      assertThat(validateBalanceFactorValues(tree.root)).isTrue();

      if (i == PRINT_SZ) {
        System.out.println(tree);
      }
    }
  }

  // Make sure all balance factor values are either -1, 0 or +1
  static boolean validateBalanceFactorValues(AVLTreeRecursive<Integer>.Node node) {
    if (node == null) return true;
    if (node.bf > +1 || node.bf < -1) return false;
    return validateBalanceFactorValues(node.left) && validateBalanceFactorValues(node.right);
  }

  @Test
  public void testRandomizedValueInsertionsAgainstTreeSet() {

    TreeSet<Integer> set = new TreeSet<>();
    for (int i = 0; i < TEST_SZ; i++) {
      int v = randValue();

      assertThat(tree.insert(v)).isEqualTo(set.add(v));
      assertThat(tree.size()).isEqualTo(set.size());
      assertThat(tree.validateBSTInvarient(tree.root)).isTrue();
    }
  }

  @Test
  public void testTreeHeight() {
    for (int n = 1; n <= TEST_SZ; n++) {

      tree.insert(randValue());
      double height = tree.height();

      // Get an upper bound on what the maximum height of
      // an AVL tree should be. Values were taken from:
      // https://en.wikipedia.org/wiki/AVL_tree#Comparison_to_other_structures
      double c = 1.441;
      double b = -0.329;
      double upperBound = c * (Math.log(n + 2.0) / Math.log(2)) + b;

      assertThat(height).isLessThan(upperBound);
    }
  }

  @Test
  public void randomRemoveTests() {
    TreeSet<Integer> ts = new TreeSet<>();
    for (int i = 0; i < TEST_SZ; i++) {

      List<Integer> lst = genRandList(i);
      for (Integer value : lst) {
        tree.insert(value);
        ts.add(value);
      }
      Collections.shuffle(lst);

      // Remove all the elements we just placed in the tree.
      for (int j = 0; j < i; j++) {

        Integer value = lst.get(j);

        assertThat(tree.remove(value)).isEqualTo(ts.remove(value));
        assertThat(tree.contains(value)).isFalse();
        assertThat(tree.size()).isEqualTo(i - j - 1);
      }

      assertThat(tree.isEmpty()).isTrue();
    }
  }

  private void runPerfTest(int size) {
    System.out.println("size: " + size);
    AVLTreeRecursiveOptimized<Integer> treeOptimized = new AVLTreeRecursiveOptimized<>();

    List<Integer> lst = genRandList(size);

    long start = System.nanoTime();
    for (Integer value : lst) {
      tree.insert(value);
    }
    long end = System.nanoTime();
    System.out.println("AVLTreeRecursive          Time: " + (end - start));

    start = System.nanoTime();
    for (Integer value : lst) {
      treeOptimized.insert(value);
    }
    end = System.nanoTime();
    System.out.println("AVLTreeRecursiveOptimized Time: " + (end - start));
  }

  @Test
  public void testComparePerfTest1() {
    runPerfTest(TEST_SZ / 10);
  }

  @Test
  public void testComparePerfTest2() {
    runPerfTest(TEST_SZ);
  }

  @Test
  public void testComparePerfTest3() {
    runPerfTest(TEST_SZ * 10);
  }

  @Test
  public void testComparePerfTest4() {
    runPerfTest(TEST_SZ * 100);
  }

  static List<Integer> genRandList(int sz) {
    List<Integer> lst = new ArrayList<>(sz);
    for (int i = 0; i < sz; i++) lst.add(i); // unique values.
    Collections.shuffle(lst);
    return lst;
  }

  public static int randValue() {
    return (int) (Math.random() * MAX_RAND_NUM * 2) + MIN_RAND_NUM;
  }
}
