package com.example.Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;


public class Tetris extends JFrame implements KeyListener {
    // 属性声明区
    static final int line = 26;
    static final int column = 15;
    static JTextArea[][] text;
    static int[][] data;
    static boolean isRunning = true;
    static JLabel labelGameStatus;
    static JLabel labelGameScore;
    static JLabel labelGameRestScore;
    // 方块刷新坐标
    static int x;
    static int y;
    static Block block;
    static int time = 700;
    static int score = 0;
    // 方法声明区
    // 构造函数进行各种参数以及游戏界面的初始化操作
    public Tetris(){
        text = new JTextArea[line][column];
        data = new int[line][column];
        labelGameScore = new JLabel("游戏得分：0");
        labelGameStatus = new JLabel("游戏状态：游戏中");
        labelGameRestScore = new JLabel("距离通关：还剩2000分");
        initGamePanel();
        initStatusPanel();
        initWindow();
    }
    // 窗口初始化函数
    public void initWindow(){
        this.setSize(600,900);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("俄罗斯方块");
    }
    // 游戏面板初始化函数
    public void initGamePanel(){
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(line,column,1,1 ));
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < column; j++) {
                // 设置文本域的行列数
                text[i][j] = new JTextArea(line,column);
                // 设置背景颜色
                text[i][j].setBackground(Color.white);
                // 设置键盘监听
                text[i][j].addKeyListener(this);
                // 设置边界
                if( j==0 || j==column-1 || i==line-1){
                    text[i][j].setBackground(Color.black);
                    data[i][j] = 1;
                }
                text[i][j].setEditable(false);

                jPanel.add(text[i][j]);

            }
        }
        this.setLayout(new BorderLayout());
        this.add(jPanel,BorderLayout.CENTER);
    }
    // 状态面板初始化函数
    public void initStatusPanel(){
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new GridLayout(3,column));
        labelGameStatus.setForeground(Color.RED);
        labelGameScore.setForeground(Color.BLUE);
        labelGameRestScore.setForeground(Color.ORANGE);
        statusPanel.add(labelGameStatus);
        statusPanel.add(labelGameScore);
        statusPanel.add(labelGameRestScore);
        this.add(statusPanel,BorderLayout.NORTH);
    }

    public static void GenerateBlock(){
        Random random = new Random();
        int n = random.nextInt(4);
        int m;
        switch (n){
            case 0:
                block = new BlockBlock();
                break;
            case 1:
                m = random.nextInt(2);
                block = new BlockZ(m);
                break;
            case 2:
                m = random.nextInt(2);
                block = new BlockLine(m);
                break;
            case 3:
                m = random.nextInt(4);
                block  = new BlockPlane(m);
                break;
        }
    }
    public static void runGame() throws InterruptedException {
        // 随机数选择生成哪个形状
        GenerateBlock();
        x = 0;
        y = 7;
        for (int i = 0; i < line; i++) {
            Thread.sleep(time);

            if(!canFall(x,y)){
                // 改变不可下落方块区域的值 改为1 即可说明此处有方块
                changeData(x,y);
                for(int j = x; j < x+4 ; j++){
                    int sum=0;
                    for (int k = 1; k < column-1; k++) {
                        if(data[j][k]==1){
                            sum++;
                        }
                    }
                    //判断消除
                    if(sum==column-2) {
                        removeLine(j);
                    }
                }
                for (int j = 1; j < column-1; j++) {
                    if(data[0][j]==1){
                        isRunning = false;
                        break;
                    }
                }
                // 此方块无法下落 退出循环进行下一个块的生成
                break;
            }
            else{
                x++;
                fall(x,y);
            }
        }

    }

    private static void removeLine(int row) throws InterruptedException {
        //从被删除的行开始，一行一行向下覆盖
        for(int i=row; i>0; i--){
            System.arraycopy(data[i - 1], 1, data[i], 1, column - 1 - 1);
        }
        update(row);

        //删除后进行加速
        time = (int) (time/1.1);

        //加分
        score += 100;
        labelGameScore.setText("游戏得分："+score);
        labelGameRestScore.setText("距离通关还剩"+ (2000-score) +"分");

        //如果游戏得分达到2000分，则显示胜利信息
        if(score == 2000){
            //绘制心形
            drawHeart(x,y);
            //停止游戏
            isRunning = false;
            //设置游戏状态为胜利
            labelGameStatus.setText("你赢了！");
        }
    }

    private static void drawHeart(int x, int y) throws InterruptedException {
        for (int k = 0; k < 4; k++) {
            for (int i = 0; i < line-1; i++) {
                for (int j = 1; j < column-1; j++) {
                    if(k % 2 == 0) {
                        text[i][j].setBackground(Color.white);
                    }
                    else{
                        text[i][j].setBackground(Color.black);
                    }
                }
            }
            Thread.sleep(500);
        }
        Thread.sleep(500);

        int start = 0,end = 0;
        for (int j = 3; j < 12; j++) {
            end = switch (j) {
                case 3, 11 -> {
                    start = 11;
                    yield 12;
                }
                case 4, 10 -> {
                    start = 10;
                    yield 13;
                }
                case 5, 9 -> {
                    start = 10;
                    yield 14;
                }
                case 6, 8 -> {
                    start = 11;
                    yield 15;
                }
                case 7 -> {
                    start = 12;
                    yield 16;
                }
                default -> end;
            };
            for (int i = start; i <= end; i++) {
                text[i][j].setBackground(Color.red);
            }
        }
    }

    private static void changeData(int m, int n) {
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if((temp & block.BlockType)!=0){
                   data[m][n] = 1;
                }
                n++;
                temp >>=1;
            }
            m++;
            n -= 4;
        }
    }
    public static void fall(int m, int n){
        if(m>0){
            //清除上一行
            clear(m-1,n);
        }

        draw(m,n);
    }
    private static boolean canFall(int m, int n) {
        // 以4*4方块的左上角格子为坐标
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if((temp & block.BlockType)!=0){
                    if(data[m+1][n]==1){
                        return false;
                    }
                }
                n++;
                temp >>=1;
            }
            m++;
            n -= 4;
        }
        //可以下落
        return true;
    }

    public static void update(int row){
        for (int i = row; i > 0; i--) {
            for (int j = 1; j < column-1; j++) {
                if(data[i][j]==1){
                    text[i][j].setBackground(Color.BLUE);
                }
                else{
                    text[i][j].setBackground(Color.white);
                }
            }
        }
    }

    public static void clear(int m,int n){
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if((temp & block.BlockType)!=0){
                    text[m][n].setBackground(Color.white);
                }
                n++;
                temp >>=1;
            }
            m++;
            n -= 4;
        }
    }

    public static void draw(int m,int n){
        //绘制块
        int temp = 0x8000;
        //遍历4*4的格子将1的地方绘制为蓝色 最终可完成在4*4格子内构造指定形状
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if((temp & block.BlockType)!=0){
                    text[m][n].setBackground(Color.BLUE);
                }
                n++;
                temp >>=1;
            }
            m++;
            n -= 4;
        }
    }

    public static void startGame() throws InterruptedException {
        while (isRunning) {
            runGame();
        }
        labelGameStatus.setText("游戏状态：游戏结束");
    }

    public static boolean canTransform(int type,int m,int n){
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if((temp & type)!=0){
                    if (data[m][n]==1){
                        return false;
                    }
                }
                n++;
                temp >>=1;
            }
            m++;
            n -= 4;
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        Tetris tetris = new Tetris();
        startGame();

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar()==KeyEvent.VK_SPACE){
            if(!isRunning){
                return;
            }
            if(!canTransform(block.nextType(),x,y)){
                return;
            }
            clear(x,y);
            block.transform();
            draw(x,y);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==37){
//            System.out.println("press left");
            if(!isRunning){
                return;
            }
            int m = x;
            int n = y;
            int num = 20;
            int temp = 0x8000;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if((temp & block.BlockType)!=0){
                        if(n<num){
                            num = n;
                        }
                    }
                    n++;
                    temp >>=1;
                }
                m++;
                n -= 4;
            }
            if(num<=1){
                return;
            }
            temp = 0x8000;
            for (int i = x; i < x+4; i++) {
                for (int j = y; j < y+4; j++) {
                    if((temp & block.BlockType) != 0){
                        if(data[i][j-1]==1){
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            //清除当前块
            clear(x,y);
            //左移并重新画出
            y--;
            draw(x,y);
        }

        if(e.getKeyCode()==39){
            if(!isRunning){
                return;
            }
            //因为x,y是左上角坐标，所以判断右边时需要找到块中的最右侧坐标
            int m = x;
            int n = y;
            int num = 1;
            int temp = 0x8000;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if((temp & block.BlockType)!=0){
                        if(n>num){
                            num = n;
                        }
                    }
                    n++;
                    temp >>=1;
                }
                m++;
                n -= 4;
            }

            if(num>=column-2){
                return;
            }

            temp = 0x8000;
            for (int i = x; i < x+4; i++) {
                for (int j = y; j < y+4; j++) {
                    if((temp & block.BlockType) != 0){
                        if(data[i][j+1]==1){
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            //清除当前块
            clear(x,y);
            //左移并重新画出
            y++;
            draw(x,y);
        }

        if(e.getKeyCode()==40){
//            System.out.println("press down");
            if(!isRunning){
                return;
            }

            if(!canFall(x,y)){
                return;
            }
            clear(x,y);
            x++;
            draw(x,y);
        }

    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}