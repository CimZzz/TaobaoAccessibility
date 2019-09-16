(function(){window.CimZzz = {
    m: function m(e, t, o, n) {
        function i() {
            window.goldlog && window.goldlog.record && window.goldlog.record(e, t, o, n);
        }
        "EXP" == t ? window.goldlog && window.goldlog.record ? i() : setTimeout(function() {
            m(e, t, o, n)
        }, 200) : i();
    },
    i: function i(isFirst, value) {
        if(isFirst == "true")
            window.CimZzz.m("/alimama.nologinredbox.h5_nologin_input", "CLK", "", "H1513818745");
        var t = document.querySelector("#J_MMREDBOX_noCaptchaCon .button")
          , o = document.querySelector("#J_MMREDBOX_TB_ID")
          , n = MM_RED_CORE.util.trim(value);
          window.MM_RED_CORE.TBINPUT_VAL = n,
          o.value = n,
        NoCaptcha.setEnabled(!0),
        o.placeholder = "\u8bf7\u8f93\u5165\u60a8\u7684\u6dd8\u5b9d\u8d26\u53f7",
        window.CimZzz.s(document.getElementById("J_MMREDBOX_TB_ID_CON_WRAP"), "error_animation"),
        window.CimZzz.s(document.getElementById("J_MMREDBOX_TB_ID"), "red_placeholder");
    },
    ii: function i(isFirst, value) {
        if(isFirst == "true")
            window.CimZzz.m("/alimama.nologinredbox.h5_nologin_input", "CLK", "", "H1513818745");
        var t = document.querySelector("#J_MMREDBOX_noCaptchaCon .button")
          , o = document.querySelector("#J_MMREDBOX_TB_ID")
          , n = MM_RED_CORE.util.trim(value);
          window.MM_RED_CORE.TBINPUT_VAL += n,
          o.value += n,
        NoCaptcha.setEnabled(!0),
        o.placeholder = "\u8bf7\u8f93\u5165\u60a8\u7684\u6dd8\u5b9d\u8d26\u53f7",
        window.CimZzz.s(document.getElementById("J_MMREDBOX_TB_ID_CON_WRAP"), "error_animation"),
        window.CimZzz.s(document.getElementById("J_MMREDBOX_TB_ID"), "red_placeholder");
    },
    s: function s(e, t) {
        var o = window.CimZzz.d(e)
          , n = o.indexOf(t);
        n > -1 && o.splice(n, 1),
        e.className = o.join(" ");
    },
    d: function d(e) {
        return (e.className || "").split(/\s+/g);
    },
    z: function z(code) {
        var isExist = document.getElementById("J_MMREDBOX_noCaptchaCon") != null;
        window.tskcj.pageSuccess(code, String(isExist));
    },
    xc: function xc(code, needCover) {
        var t = document.getElementById("J_MMREDBOX_DIALOG");
        if(t == null || t.style.visibility != "visible") {
            if(needCover == "true")
                window.MM_RED_CORE.cover();
            window.tskcj.dialog(code, "false");
            return;
        }
    
        window.tskcj.dialog(code, "true");
    },
    bs: function bs(code, phoneNum) {
        window.tskcj.input(code, String(window.MM_RED_CORE.TBINPUT_VAL == phoneNum));
    },
    kt: function kt(code) {
        var width = document.documentElement.clientWidth;
        var rect = document.getElementById("J_MMREDBOX_COVER_LOGO").getBoundingClientRect();
        window.tskcj.scroll(code, String(width), String(rect.left), String(rect.top), String(rect.right), String(rect.bottom))
    },
    mcl: function mcl() {
        var n = NoCaptcha.__nc;
        n.fire("actionend");
        n.fsm.can("verify") && n.fsm.verify();
    },
    ctl: function ctl() {
        window.tskcj.desc(document.getElementById("J_MMREDBOX_DIALOG").innerHTML);
    },
    valid: function valid(code) {
        var elem = document.getElementById("J_MMREDBOX_INFO_TITLE");
        if(elem != null && elem.innerText == "今日抽奖机会已用完")
            window.tskcj.complete(code);
        else window.tskcj.has(code, String(document.getElementById("J_MMREDBOX_RED_FACE") != null));
    }
}})()