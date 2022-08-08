package indi.odin.consumer;

/**
 * 处理结果反馈
 *
 * @author <a href="mailto:maimengzzz@gmail.com">韩超</a>
 * @since 1.0.0
 */
public enum HandleResponse {
    SUCCESS(0),
    FAILURE(1),
    NEED_RESEND(2),
    ;

    private int code;

    HandleResponse(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
