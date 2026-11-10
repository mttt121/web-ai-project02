package org.example.exception;


/**
 * 删除操作被拒绝时抛出（例如：班级下还有学生，不允许删除）
 */
public class DeletionNotAllowedException extends RuntimeException {

    public DeletionNotAllowedException(String message) {
        super(message);
    }
}
