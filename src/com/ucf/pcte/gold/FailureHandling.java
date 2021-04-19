package com.ucf.pcte.gold;


public enum FailureHandling
{
    STOP_ON_FAILURE("STOP_ON_FAILURE", 0), 
    CONTINUE_ON_FAILURE("CONTINUE_ON_FAILURE", 1), 
    OPTIONAL("OPTIONAL", 2);

    private FailureHandling(final String name, final int ordinal) {
    }
}