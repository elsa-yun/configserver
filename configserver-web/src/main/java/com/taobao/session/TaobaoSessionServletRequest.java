package com.taobao.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * @author huangshang
 *
 */
public class TaobaoSessionServletRequest extends HttpServletRequestWrapper {

    private TaobaoSession session;

    public TaobaoSessionServletRequest(HttpServletRequest request) {
        super(request);
    }

    public void setSession(TaobaoSession session) {
        this.session = session;
    }

    @Override
    public HttpSession getSession() {
        return this.session;
    }

}
