
/**
 * @Classname UnionFind
 * @Description 并查集
 * @Date 2020/1/2 10:49
 * @Author SonnSei
 */
class UnionFind{
    private int[] rank;
    private int[] parent;

    public UnionFind(int size) {
        rank = new int[size];
        parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }

    public int getSize() {
        return parent.length;
    }

    //查找元素p对应的集合编号，也就是根节点的编号
    private int find(int p) {
        if(p<0 || p>=parent.length)
            throw new IllegalArgumentException("p is out of bound");
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public boolean isConnected(int p, int q) {
        return find(p)==find(q);
    }

    public void unionElements(int p, int q) {
        int pRoot = find(p);
        int qRoot = find(q);
        if(pRoot == qRoot)
            return ;
        if(rank[pRoot]<rank[qRoot])
            parent[pRoot] = qRoot;
        else if (rank[pRoot]>rank[qRoot])
            parent[qRoot] = pRoot;
        else{
            parent[pRoot] = qRoot;
            rank[qRoot]+=1;
        }
    }

    public int groups(){
        int ret = 0;
        for (int i = 0; i < parent.length; i++) {
            if(parent[i] == i)
                ret ++;
        }
        return ret;
    }
}