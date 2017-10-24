/**
 * Created by Caby on 5/18/17.
 */

layui.define(['jquery'], function(exports) {
    var $ = layui.jquery;
    var _sessionInvalided = false;
    function canRequest() {
        return _sessionInvalided === false;
    }
    // check login status from request result
    function _checkLoginStatus(result) {
        if (!result) return;
        if (result.code == -5) {
            if (_sessionInvalided === false) {
                window.location.href = '/system/login.html?redirect=' + _urlencode(window.location.href);
                _sessionInvalided = true;
            }
            return false;
        }
        return true;
    }
    function handleResult(result, callback, noCheckLoginStatus) {
        callback = callback || $.noop;
        if (!noCheckLoginStatus && !_checkLoginStatus(result)) {
            return;
        }
        if (!result) {
            result = {code: -1, msg: '未知错误'};
        }
        callback(result);
    }
    // request data with post method
    function _post(url,  data, callback, noCheckLoginStatus) {
        if (!canRequest()) return;
        callback = callback || $.noop;
        if (!url) {
            callback({code: -1, msg: '请求地址为空'});
            return;
        }
        $.post(url, data, function (result) { handleResult(result, callback, noCheckLoginStatus) }, 'json').fail(function () {
            callback({code: -1, msg: '连接不到服务器'});
        });
    }
    // request data with get method
    function _get(url, data, callback, noCheckLoginStatus) {
        if (!canRequest()) return;
        callback = callback || $.noop;
        if (!url) {
            callback({code: -1, msg: '请求地址为空'});
            return;
        }
        $.get(url, data, function (result) { handleResult(result, callback, noCheckLoginStatus) }, 'json').fail(function () {
            callback({code: -1, msg: '连接不到服务器'});
        });
    }
    // request data with put method
    function _put(url, data, callback, noCheckLoginStatus) {
        if (!canRequest()) return;
        callback = callback || $.noop;
        if (!url) {
            callback({code: -1, msg: '请求地址为空'});
            return;
        }
        $.ajax({
            url: url,
            type: 'PUT',
            data: data,
            dataType: 'json',
            success: function (result) { handleResult(result, callback, noCheckLoginStatus) }
        }).fail(function () {
            callback({code: -1, msg: '连接不到服务器'});
        });
    }
    // request data with update method
    function _update(url, data, callback, noCheckLoginStatus) {
        if (!canRequest()) return;
        callback = callback || $.noop;
        if (!url) {
            callback({code: -1, msg: '请求地址为空'});
            return;
        }
        $.ajax({
            url: url,
            type: 'UPDATE',
            data: data,
            dataType: 'json',
            success: function (result) { handleResult(result, callback, noCheckLoginStatus) }
        }).fail(function () {
            callback({code: -1, msg: '连接不到服务器'});
        });
    }
    // request data with delete method
    function _delete(url, data, callback, noCheckLoginStatus) {
        if (!canRequest()) return;
        callback = callback || $.noop;
        if (!url) {
            callback({code: -1, msg: '请求地址为空'});
            return;
        }
        $.ajax({
            url: url,
            type: 'DELETE',
            data: data,
            dataType: 'json',
            success: function (result) { handleResult(result, callback, noCheckLoginStatus) }
        }).fail(function () {
            callback({code: -1, msg: '连接不到服务器'});
        });
    }
    // ger url from window.location.href
    function _getCurrentUrl() {
        var url = window.location.href;
        var endIdx = url.indexOf('?');
        if (endIdx == -1) endIdx = url.length;

        var scheme = 'http://';
        var startIdx = url.indexOf(scheme);
        if (startIdx < 0) {
            scheme = 'https://';
            startIdx = url.indexOf(scheme);
        }
        if (startIdx >= 0) {
            startIdx += scheme.length;
            startIdx = url.indexOf('/', startIdx);
        } else {
            startIdx = 0;
        }
        return url.substring(startIdx, endIdx);
    }
    // url encode
    function _urlencode(url) {
        url = (url + '').toString();
        return encodeURIComponent(url)
            .replace(/!/g, '%21')
            .replace(/'/g, '%27')
            .replace(/\(/g, '%28')
            .replace(/\)/g, '%29')
            .replace(/\*/g, '%2A');
    }
    // url decode
    function _urldecode(url) {
        url = (url + '').toString();
        return decodeURIComponent(
            url.replace(/%21/g, '!')
                .replace(/%27/g, "'")
                .replace(/%28/g, '(')
                .replace(/%29/g, ')')
                .replace(/%2A/g, '*')
                .replace(/%20/g, '+'));
    }
    // get params from url
    function _getParamsInURL(url) {
        var params = {};
        var idx = url.indexOf("?");
        if (idx != -1) {
            var paramsStr = url.substr(idx + 1).split("&");
            for(var i = 0; i < paramsStr.length; ++i) {
                var item = paramsStr[i].split("=");
                params[item[0]] = _urldecode(item[1]) || '';
            }
        }
        return params;
    }
    // remove empty chat in a string form left and right
    function _trim(str) {
        return str && str.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
    }
    // is android
    function _isAndroid() {
        var u = navigator.userAgent;
        return u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
    }
    // is iOS
    function _isiOS() {
        return !!navigator.userAgent.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    }
    // isArrayEqual
    function _isArrayEqual(arr1, arr2) {
        if (typeof arr1 != 'object' || typeof arr2 != 'object') return false;
        if (!arr1 && !arr2) return true;
        return (arr1.length == arr2.length)
            && arr1.every(function(element, index) {
                return element === arr2[index];
            });
    }
    // password salt
    function _getPasswordSalt() {
        return 'w9h@f4p3';
    }

    // exports
    var func =  {
        post: _post,
        get: _get,
        put: _put,
        update: _update,
        delete: _delete,
        getCurrentUrl: _getCurrentUrl,
        urlencode: _urlencode,
        urldecode: _urldecode,
        getParamsInURL: _getParamsInURL,
        trim: _trim,
        isAndroid: _isAndroid,
        isiOS: _isiOS,
        isArrayEqual: _isArrayEqual,
        getPasswordSalt: _getPasswordSalt
    };
    exports('commonUtils', func);
});