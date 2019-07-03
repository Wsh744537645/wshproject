package com.mpool.common.model.pool.utils;

public interface MathShareMinute {

	Long getShareAccept();

	Long getShareReject();

	default double getHashAcceptT() {
		return MathShare.MathShareMinuteDouble(getShareAccept());
	}

	default double getHashRejectT() {
		return MathShare.MathShareMinuteDouble(getShareReject());
	}
}
