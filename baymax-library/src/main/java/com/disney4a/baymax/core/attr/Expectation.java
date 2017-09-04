package com.disney4a.baymax.core.attr;

/**
 * Created by Administrator on 2017/8/21 0021.
 * 期望，愿望，希望能有些回报。
 * 回调接口
 * @see Correspondents#setExpectation(Expectation)
 */

public interface Expectation<T extends Repay> {
    /**
     * 做些事情
     * @param correspondents
     */
    void doSomething(Correspondents correspondents);

    /**
     * 回报
     * @param repay
     */
    void onRepay(T repay);
}
