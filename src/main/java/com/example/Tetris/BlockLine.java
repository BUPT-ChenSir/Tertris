package com.example.Tetris;

public class BlockLine extends Block {
    public BlockLine(int m) {
        BlockTypeKey = m;
        BlockType = getOneOfBlockType(m);
    }

    @Override
    public void transform() {
        BlockType = getOneOfBlockType(BlockTypeKey);
    }

    @Override
    public int getOneOfBlockType(int m) {
        return switch (m) {
            case 0 -> 0x000F;
            case 1 -> 0x8888;
            default -> throw new IllegalStateException("Unexpected value: " + m);
        };
    }

    @Override
    public int nextType() {
        if(BlockTypeKey<1){
            BlockTypeKey++;
        }else {
            BlockTypeKey = 0;
        }

        return getOneOfBlockType(BlockTypeKey);
    }
}
