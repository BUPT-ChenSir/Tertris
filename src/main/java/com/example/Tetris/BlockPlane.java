package com.example.Tetris;

public class BlockPlane extends Block {
    public BlockPlane(int m){
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
                case 0 -> 0x004E;
                case 1 -> 0x0464;
                case 2 -> 0x00E4;
                case 3 -> 0x04c4;
                default -> throw new IllegalStateException("Unexpected value: " + m);
            };

    }

    @Override
    public int nextType() {
        if(BlockTypeKey<3){
            BlockTypeKey++;
        }else {
            BlockTypeKey = 0;
        }
        return getOneOfBlockType(BlockTypeKey);
    }


}

