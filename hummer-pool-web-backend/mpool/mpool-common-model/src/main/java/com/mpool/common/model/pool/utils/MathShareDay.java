package com.mpool.common.model.pool.utils;

/**
 * 接受率和拒绝率是按天计算
 */
public interface MathShareDay {
	Long getShareAccept();

	Long getShareReject();

	default double getHashAcceptT() {
		return MathShare.MathShareDayDouble(getShareAccept());
	}

	default double getHashRejectT() {
		return MathShare.MathShareDayDouble(getShareReject());
	}

	default double getRate(){
		double hashAcceptT=getHashAcceptT();
		double hashRejectT=getHashRejectT();
		double sum= hashAcceptT +hashRejectT;
		if(sum == 0d){
			return 0;
		}
		return hashRejectT / sum *100;
	}
}
