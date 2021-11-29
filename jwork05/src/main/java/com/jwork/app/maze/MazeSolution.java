package com.jwork.app.maze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MazeSolution {

    private int[][] maze;
    private Queue<Integer>[][] steps; // 0-up, 1-down, 2-left, 3-right
    private int[][] flags; // 标记遍历过程
    private boolean flag; // 标记钥匙
    private int maxKeysNum;
    private int curKeysNum;
    
    @SuppressWarnings("unchecked")
    public MazeSolution(int[][] maze, int maxKeysNum) {
        this.maze = maze;
        this.maxKeysNum = maxKeysNum;
        this.curKeysNum = 0;
        this.flag = true;
        steps = (Queue<Integer>[][])new Queue[maze.length][maze[0].length];
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                steps[i][j] = new LinkedList<Integer>();
            }
        }
        flags = new int[maze.length][maze[0].length];
    }

    public void calculate() {
        calculate(0, 0);
    }

    public void calculate(int x, int y) {
        for(int i = 0; i < maze.length; i++) {
            for(int j = 0; j < maze[0].length; j++) {
                steps[i][j].clear();
                flags[i][j] = 0;
            }
        }

        dfs(new Node(x, y));
    }

    private boolean dfs(Node cur) {
        if (maze[cur.y][cur.x] == 2 && flag) {
            curKeysNum++;
            maze[cur.y][cur.x] = 1;
        }
        if (curKeysNum == maxKeysNum && isEnding(cur)) {
            flags[cur.y][cur.x] = 2;
            return true;
        } else if (curKeysNum == maxKeysNum && flag) {
            for(int i = 0; i < maze.length; i++) {
                for(int j = 0; j < maze[0].length; j++) {
                    flags[i][j] = 0;
                }
            }
            flag = false;
        }
        flags[cur.y][cur.x] = 1;
        // if (maze[cur.y][cur.x] == 2 && flag) {
        //     curKeysNum++;
        //     maze[cur.y][cur.x] = 1;
        // }
        ArrayList<Node> neighbors = findNextNodes(cur);
        for (Node next: neighbors) {
            steps[cur.y][cur.x].add(step(cur, next));
            if(dfs(next)) {
                flags[cur.y][cur.x] = 2;
                return true;
            }
            steps[next.y][next.x].add(step(next, cur));
        }
        flags[cur.y][cur.x] = 2;
        return false;
    }

    private ArrayList<Node> findNextNodes(Node node) {
        ArrayList<Node> nodes = new ArrayList<>();

        int x = node.x, y = node.y;
        if (x < maze[0].length - 1 && maze[y][x+1] > 0 && flags[y][x+1] == 0) {
            nodes.add(new Node(x + 1, y));
        }
        if (y < maze.length - 1 && maze[y+1][x] > 0 && flags[y+1][x] == 0) {
            nodes.add(new Node(x, y + 1));
        }
        if (x > 0 && maze[y][x-1] > 0 && flags[y][x-1] == 0) {
            nodes.add(new Node(x - 1, y));
        }
        if (y > 0 && maze[y-1][x] > 0 && flags[y-1][x] == 0) {
            nodes.add(new Node(x, y - 1));
        }

        return nodes;
    }

    private int step(Node from, Node to) {
        int mx = to.x - from.x, my = to.y - from.y;
        int step = -1;
        if (mx == 0 && my == -1) {
            step = 0;
        } else if (mx == 0 && my == 1) {
            step = 1;
        } else if (mx == -1 && my == 0) {
            step = 2;
        } else if (mx == 1 && my == 0) {
            step = 3;
        }
        return step;
    }

    public void checkStep(int x, int y, int mx, int my) {
        try {
            int step = step(new Node(x-mx, y-my), new Node(x, y));
            int autoStep = steps[y-my][x-mx].poll();
            if (autoStep != step) {
                calculate(x, y);
            }
        } catch (Exception e) {
            calculate(x, y);
        }
    }

    public int getStep(int x, int y) {
        int step = -1;
        try {
            step = steps[y][x].poll();
        } catch(Exception e) {
            step = -1;
        }
        return step;
    }

    private boolean isEnding(Node n) {
        return n.x == maze[0].length - 1 && n.y == maze.length - 1;
    }
}
