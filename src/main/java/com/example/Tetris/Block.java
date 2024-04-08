package com.example.Tetris;

abstract class Block {
    int BlockType;
    int BlockTypeKey;
    private int positionLine;
    private int positionColumn;

    public int getBlockType() {
        return BlockType;
    }

    public void setBlockType(int blockType) {
        BlockType = blockType;
    }

    public int getPositionLine() {
        return positionLine;
    }

    public void setPositionLine(int positionLine) {
        this.positionLine = positionLine;
    }

    public int getPositionColumn() {
        return positionColumn;
    }

    public void setPositionColumn(int positionColumn) {
        this.positionColumn = positionColumn;
    }

    abstract public void transform();
    abstract public int getOneOfBlockType(int m);
    abstract public int nextType();

    public Block(){
        // 方块生成的初始点
        positionLine = 0;
        positionColumn = 6;

    }



}

