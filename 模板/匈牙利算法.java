import java.util.Arrays;

class Hungary {

    boolean[] used;
    boolean[][] map;
    int[] matchX;
    int[] matchY;
    int[] V1;
    int[] V2;

    public int hungary() {
        int ret = 0;
        for (int i = 0; i < V1.length; i++) {
            Arrays.fill(used, false);
            if (find(V1[i]))ret++;
        }
        for (int i = 0; i < matchX.length; i++) {
            if(matchX[i]==-1)continue;
            System.out.println(i+"--"+matchX[i]);
        }
        return ret;
    }

    public Hungary(boolean[][] map,int[] v1,int[] v2) {
        int n = v1.length+V2.length;
        used = new boolean[n];
        this.map = map;
        matchX = new int[n];
        matchY = new int[n];
        Arrays.fill(matchX, -1);
        Arrays.fill(matchY, -1);
        V1 = v1;
        V2 = v2;
    }

    private boolean find(int x) {
        for (int i = 0; i< V2.length; i++) {
            int y = V2[i];
            if (map[x][y] && !used[y]) {
                used[y]=true;
                if (matchY[y] == -1 || find(matchY[y])) {
                    matchX[x]=y;
                    matchY[y]=x;
                    return true;
                }
            }
        }
        return false;
    }
}