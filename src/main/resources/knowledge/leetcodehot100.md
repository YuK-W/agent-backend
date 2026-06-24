# LeetCode Hot 100 题解

## 1. 两数之和 (Two Sum)

**题目**：给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出和为目标值 target 的那两个整数，并返回它们的数组下标。

**解法**：
- 使用哈希表（HashMap）存储遍历过的元素
- 遍历数组，对于每个元素，计算 target - nums[i]
- 如果差值在哈希表中存在，返回对应的下标和当前下标
- 如果不存在，将当前元素存入哈希表

**时间复杂度**：O(n)
**空间复杂度**：O(n)

**代码示例（Java）**：
```java
public int[] twoSum(int[] nums, int target) {
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return new int[]{};
}