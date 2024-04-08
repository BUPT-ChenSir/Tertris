package com.example.Tetris;

public class BlockBlock extends Block{
    @Override
    public void transform() {
        BlockType = 0x00cc;
    }

    @Override
    public int getOneOfBlockType(int m) {
        return 0x00cc;
    }

    @Override
    public int nextType() {
        return 0x00cc;
    }

    public BlockBlock(){
        BlockType = getOneOfBlockType(1);
    }
}
