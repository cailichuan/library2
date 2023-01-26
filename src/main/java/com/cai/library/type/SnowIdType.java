package com.cai.library.type;

public enum SnowIdType {
    BOOK(1L,1L),
    USER(1L,2L),
    B_AND_L(1L,3L);

    private Long machineId;
    private Long workId;

    private SnowIdType(Long machineId,Long workId){
        this.machineId=machineId;
        this.workId=workId;

    }

    public Long getMachineId() {
        return machineId;
    }

    public Long getWorkId(){
        return workId;
    }
}
