# 哈希链表的手动实现
```java
class LRUCache {
    class Node {
        public Node pre;
        public Node next;
        public int key;
        public int value;

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node head;
    private Node end;

    //缓存存储上限
    private int limit;

    private HashMap<Integer, Node> hashMap;

    public LRUCache(int limit) {
        this.limit = limit;
        hashMap = new HashMap<>();
    }

    public int get(int key) {
        Node node = hashMap.get(key);
        if (node == null) {
            return -1;
        }
        refreshNode(node);
        return node.value;
    }

    private void refreshNode(Node node) {
        //如果访问的是尾节点，则无需移动节点
        if (node == end) {
            return;
        }
        //移除节点
        removeNode(node);
        //重新插入节点
        addNode(node);
    }

    private void addNode(Node node) {
        if (end != null) {
            end.next = node;
            node.pre = end;
            node.next = null;
        }
        end = node;
        if (head == null) {
            head = node;
        }
    }

    public void put(int key, int value) {
        Node node = hashMap.get(key);
        if (node == null) {
            if (hashMap.size() >= limit) {
                int oldKey = removeNode(head);
                hashMap.remove(oldKey);
            }
            node = new Node(key, value);
            addNode(node);
            hashMap.put(key, node);
        } else {
            node.value = value;
            refreshNode(node);
        }
    }

    public void remove(String key) {
        Node node = hashMap.get(key);
        removeNode(node);
        hashMap.remove(key);
    }

    private int removeNode(Node node) {
        if (node == head && node == end) {
            head = null;
            end = null;
        } else if (node == end) {
            end = end.pre;
            end.next = null;
        } else if (node == head) {
            head = head.next;
            head.pre = null;
        } else {
            node.pre.next = node.next;
            node.next.pre = node.pre;
        }
        return node.key;
    }
}
```

# LinkedHashMap实现
```java
public class LRUCache {
    private LinkedHashMap<Integer, Integer> map;

    public LRUCache(final int capacity) {
        map = new LinkedHashMap<Integer, Integer>(capacity,0.75f,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size()>capacity;
            }
        };
    }

    public int get(int key) {
        if(map.containsKey(key))
            return map.get(key);
        return -1;
    }

    public void put(int key, int value) {
        map.put(key,value);
    }
}

```