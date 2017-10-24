/**
 * Created by Caby on 6/3/17.
 */
;(function () {
    function loginFunc() {
        var _layer = layui.layer, _utils = layui.commonUtils, _loading = layui.commonLoading,
            _md5 = layui.md5, _form = layui.form, _sha1 = layui.sha1;
        _form.on('submit(formLogin)', function(data){
            _loading.showLoading();
            _utils.post('/api/login/login',
                {username: data.field.username,
                    password: _sha1(_md5(data.field.password) + _utils.getPasswordSalt())}, function (result) {
                    _loading.hideLoading();
                    if (0 == result.code) {
                        var redirect = '/index.html';
                        var paramsInUrl = _utils.getParamsInURL(window.location.href);
                        if (paramsInUrl['redirect']) redirect = paramsInUrl['redirect'];
                        window.location.href = redirect;
                    } else {
                        _layer.alert('登录失败，' + result.msg);
                    }
                }, true);
            return false;
        });
    }

    layui.use(
        [
            'layer',
            'jquery',
            'md5',
            'commonUtils',
            'commonLoading',
            'form',
            'sha1'
        ],
        loginFunc
    );
} ());