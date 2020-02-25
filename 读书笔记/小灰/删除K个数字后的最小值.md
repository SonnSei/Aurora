# 思路
- 优先让最高位的值减小，即从左开始，如果某位的值大于其右边的值，则必须将其删除，否则它就一直在那个地方，最后结果也一定不会比删了它小

```java
public static String removeKDigits(String num, int k) {
    Stack<Character> stack = new Stack<>();
    int count = k;
    for (int i = 0; i < num.length(); i++) {
        char c = num.charAt(i);
        if(stack.isEmpty())
            stack.push(c);
        else if (count > 0 && stack.peek() > c) {
            stack.pop();
            stack.push(c);
            count--;
        } else {
            stack.push(c);
        }
    }
    StringBuilder ret = new StringBuilder();
    while (!stack.isEmpty()) {
        ret.append(stack.pop());
    }
    return ret.reverse().toString();
}
···