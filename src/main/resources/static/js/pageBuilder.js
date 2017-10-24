/**
 * Created by Caby on 5/25/17.
 */

layui.define(['jquery', 'layer', 'element', 'commonUtils', 'form', 'commonLoading', 'sha1', 'md5'], function (exports) {
    var $ = layui.jquery, _layer = layui.layer, _domElement = layui.element, _utils = layui.commonUtils,
        _form = layui.form, _loading = layui.commonLoading, _sha1 = layui.sha1, _md5 = layui.md5;
    var _hasBuilt = false;
    // 页头
    function _buildHeader() {
        var header = $('#page_header');
        if (!header) return;
        header.html(
            '<ul id="main_nav_top" class="layui-nav" lay-filter="main_nav_top">'
            + '<li class="layui-nav-item">'
            + ' <a href="javascript:;" id="page_header_title_username">...</a>'
            + ' <dl class="layui-nav-child">'
            + ' <dd><a href="javascript:;" id="page_header_btn_reset_pwd">修改密码</a></dd>'
            + ' <dd><a href="javascript:;" id="page_header_btn_logout">退出登录</a></dd>'
            + ' </dl>'
            + '</li>'
            + '</ul>'
        );
        _domElement.init();
    }
    // 左侧导航栏
    function _buildNav() {
        _utils.get('/api/navigation/navShow', {}, function (result) {
            if (result.code == 0) {
                var leftNav = $('#page_nav_left');
                if (!leftNav) return;
                var currentUrl = _utils.getCurrentUrl();
                var html = '<ul class="layui-nav layui-nav-tree layui-nav-side" id="main_nav_left" lay-filter="main_nav_left">'
                    + '<li class="layui-nav-item' + ('/index.html' == currentUrl ? ' layui-this' : '')
                    + '"><a href="/index.html">首页</a></li>';
                $.each(result.content, function (idx, navigation) {
                    var subHtml = '';
                    $.each(navigation.interfaceList, function (idxInterface, oneInterface) {
                        subHtml += '<dd' + (oneInterface.url == currentUrl ? ' class="layui-this"' : '') + '><a href="'
                            + oneInterface.url + '">' + oneInterface.name + '</a></dd>'
                    });
                    html += '<li class="layui-nav-item layui-nav-itemed">'
                        + '<a href="javascript:;">' + navigation.name + '<span class="layui-nav-more"></span></a>'
                        + '<dl class="layui-nav-child">'
                        + subHtml + '</li>';
                });
                html += '</ul>';
                leftNav.html(html);
                _domElement.init();
            }
        });
    }
    // 页脚
    function _buildFooter() {
        var footer = $('#page_footer');
        if (!footer) return;
        footer.html(
            '<div class="page_footer_container">'
            + '<span>Copyright xxxxxxxxxxxxxxxxxxxxxxx</span>'
            +'</div>'
        );
        _domElement.init();
    }
    // build all
    function _build() {
        if (_hasBuilt) return;
        _hasBuilt = true;
        _buildHeader();
        _buildNav();
        _buildFooter();

        // logout
        var bodyEle = $('body');
        bodyEle.on('click', '#page_header_btn_logout', function () {
            _layer.confirm('确定要退出登录？', function (index) {
                _layer.close(index);
                _utils.post('/api/login/logout', {}, function () {
                    window.location.href = '/login.html';
                });
            });
        });
        // reset password
        var resetPasswordDialogHtml =
            '<div id="page_header_reset_pwd_form_container">'
            +   '<form id="page_header_reset_pwd_form" class="layui-form" action="">'
            +       '<div class="layui-form-item">'
            +           '<label class="layui-form-label">旧密码：</label>'
            +           '<div class="layui-input-inline">'
            +               '<input type="password" name="oldPwd" lay-verify="required" autocomplete="off" '
            +                   'class="layui-input" value="">'
            +           '</div>'
            +       '</div>'
            +       '<div class="layui-form-item">'
            +           '<label class="layui-form-label">新密码：</label>'
            +           '<div class="layui-input-inline">'
            +               '<input type="password" name="pwd" lay-verify="required" autocomplete="off" '
            +                   'class="layui-input" value="">'
            +           '</div>'
            +       '</div>'
            +       '<div id="btn_page_header_reset_pwd_container" class="layui-form-item">'
            +           '<button id="btn_page_header_reset_pwd_confirm" class="layui-btn" lay-submit '
            +               'lay-filter="page_header_reset_pwd_confirm">确定</button>'
            +           '<button type="reset" class="layui-btn layui-btn-primary">重置</button>'
            +       '</div>'
            +   '</form>'
            + '</div>';
        var resetPasswordDialog = false;
        bodyEle.on('click', '#page_header_btn_reset_pwd', function () {
            if (resetPasswordDialog) return;
            resetPasswordDialog = _layer.open({
                type: 1,
                area: ['400px', '280px'],
                title: '添加接口',
                content: resetPasswordDialogHtml,
                cancel: closeResetPasswordDialog
            });
            _form.render();

        });
        // do reset password
        _form.on('submit(page_header_reset_pwd_confirm)', function (data) {
            _loading.showLoading();
            data.field.pwd = _sha1(_md5(data.field.pwd) + _utils.getPasswordSalt());
            data.field.oldPwd = _sha1(_md5(data.field.oldPwd) + _utils.getPasswordSalt());
            _utils.put('/api/user/resetPwd', data.field, function (result) {
                _loading.hideLoading();
                if (result.code != 0) {
                    _layer.alert(result.msg || '未知错误，请重试');
                    return;
                }
                _layer.msg('设置密码成功', {time: 1000}, function () {
                    closeResetPasswordDialog();
                });
            });
            return false;
        });
        // close reset password dialog
        function closeResetPasswordDialog() {
            if (resetPasswordDialog) {
                _layer.close(resetPasswordDialog);
                resetPasswordDialog = false;
            }
        }
        // get self info
        _utils.get('/api/user/selfInfo', {}, function (result) {
            if (result.code == 0 && result.content) {
                $('#page_header_title_username').text(result.content.username);
            }
        });
    }
    // exports var
    var func = {
        build: _build
    };

    _build();

    exports('pageBuilder', func);
});