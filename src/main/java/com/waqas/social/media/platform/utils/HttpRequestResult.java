package com.waqas.social.media.platform.utils;

public class HttpRequestResult {

    private String status;
    private String description;
    private Object data = "";
    private String refId;

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(final Object data) {
        this.data = data;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(final String refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "{status=" + status + ", description=" + description + ", data=" + data + ", refId="
                + refId + "}";
    }


}
