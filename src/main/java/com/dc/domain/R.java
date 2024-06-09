package com.dc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {
	private int code;
	private String msg;
	private T data;

	public static <T> R<T> ok(T data) {
		return new R<T>(200, "ok", data);
	}

	public static <T> R<T> error(String msg) {
		return new R<T>(500, msg, null);
	}

	public static <T> R<T> error(String msg, T data) {
		return new R<T>(500, msg, data);
	}

	public static <T> R<T> error(int code, String msg) {
		return new R<T>(code, msg, null);
	}
}
