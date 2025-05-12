package bd.org.quantum.education.common;

import bd.org.quantum.common.utils.Misc;

import java.util.EnumMap;

public enum Stage {
    SAVE,
    SUBMIT,
    APPROVE,
    PUBLISHED,
    SUSPENDED,
    CLOSED;

    public static EnumMap<Stage, String> getStageMap() {
        EnumMap<Stage, String> map = new EnumMap<>(Stage.class);
        for (Stage stage : Stage.values()) {
            map.put(stage, Misc.getReadableName(stage.name()));
        }
        return map;
    }
}