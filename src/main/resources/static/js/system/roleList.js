/**
 * Created by Caby on 6/3/17.
 */
;(function () {
    function roleListFunc() {
        var _utils = layui.commonUtils, _loading = layui.commonLoading, $ = layui.jquery, _layer = layui.layer,
            _dateFormat = layui.dateFormat, _pager = layui.laypage, _form = layui.form;

        // fetch data
        var _roleListContainer = $('#roleListContainer');
        var _roleList = {};
        function getRoleList(obj, first) {
            if (first) return;
            _roleListContainer.html('');
            var pageNo = obj.curr;
            _loading.showLoading();
            $.get('/api/role/list', {pageNo: pageNo - 1}, function (result) {
                _loading.hideLoading();
                $('.layui-table').show();
                var pageSize = 20, totalCount = 0;
                if (result.code == 0) {
                    pageNo = result.content.pageNo + 1;
                    pageSize = result.content.pageSize;
                    totalCount = result.content.roleCount;
                    $.each(result.content.roleList, function (idx, role) {
                        var tr = $('<tr class="role_info_row" data-rid="' + role.id + '"></tr>');
                        tr.append('<td>' + (idx + 1) + '</td>');
                        tr.append('<td>' + role.name + '</td>');
                        tr.append('<td>' + role.des + '</td>');
                        tr.append('<td>' + (role.status == 0 ? '正常' : '禁用') + '</td>');
                        tr.append('<td>' + _dateFormat(new Date(role.createTime), 'yyyy-mm-dd HH:MM:ss') + '</td>');
                        tr.append('<td><div class="layui-btn-group" data-rid="' + role.id + '">'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_edit_role" title="编辑">'
                            +       '<i class="layui-icon">&#xe642;</i>'
                            +   '</button>'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_delete_role" title="删除">'
                            +       '<i class="layui-icon">&#xe640;</i>'
                            +   '</button>'
                            + '</div></td>');
                        _roleListContainer.append(tr);
                        _roleList[role.id] = role;
                    });
                } else {
                    _layer.msg(result.msg ? result.msg : '获取角色列表失败');
                }
                if (!first) {
                    setTimeout(function () {
                        _pager.render({elem: 'pagination', count: totalCount, limit: pageSize, curr: pageNo, jump: getRoleList});
                    }, 0);
                }
            });
        }
        // add or edit role
        var _btnAddRole = $('#btn_add_role');
        var _addRoleHtmlContainer = $('#add_role_html_container');
        var _dialogIdx = false;
        function showEditRoleDialog(rid) {
            if (_dialogIdx !== false) return;
            rid = rid || 0;
            var roleInfo = _roleList[rid] ? _roleList[rid] : false;
            _addRoleHtmlContainer.find('input[name=rid]').attr('value', rid);
            _addRoleHtmlContainer.find('input[name=name]').attr('value', roleInfo ? roleInfo.name : '');
            _addRoleHtmlContainer.find('input[name=des]').attr('value', roleInfo ? roleInfo.des : '');
            var statusCheckbox = _addRoleHtmlContainer.find('input:checkbox[name=status]').removeAttr('checked');
            if (!roleInfo || roleInfo.status == 0) {
                statusCheckbox.attr('checked', 'checked');
            }
            _btnAddRole.addClass('layui-btn-disabled');
            _dialogIdx = _layer.open({
                type: 1,
                area: ['400px', '320px'],
                title: '添加接口',
                content: _addRoleHtmlContainer.html(),
                cancel: closeAddRoleDialog
            });
            _form.render();
        }
        // add role
        _btnAddRole.click(showEditRoleDialog);
        // confirm to add role
        _form.on('submit(confirm)', function (data) {
            _loading.showLoading();
            var requestUrl = '/api/role/add';
            if (data.field.rid && data.field.rid > 0) {
                requestUrl = '/api/role/update';
            }
            if (typeof data.field.status == 'undefined') {
                data.field.status = 1;
            }
            _utils.put(requestUrl, data.field, function (result) {
                _loading.hideLoading();
                if (result.code != 0) {
                    _layer.alert(result.msg || '未知错误');
                    return;
                }
                _layer.msg('成功', {time: 1500}, function () {
                    window.location.reload(true);
                });
            });
            return false;
        });
        // close add role dialog
        function closeAddRoleDialog() {
            if (_dialogIdx !== false) {
                _layer.close(_dialogIdx);
            }
            _btnAddRole.removeClass('layui-btn-disabled');
            _dialogIdx = false;
        }
        // edit role
        var _bodyEle = $('body');
        _bodyEle.on('click', '.btn_edit_role', function () {
            var rid = $(this).parent().attr('data-rid');
            if (!rid) {
                layer.msg('找不到角色的id，无法进行编辑，请刷新页面再试');
                return;
            }
            showEditRoleDialog(rid);
        });
        // delete role
        _bodyEle.on('click', '.btn_delete_role', function () {
            var rid = $(this).parent().attr('data-rid');
            if (!rid) {
                layer.msg('找不到角色的id，无法进行删除，请刷新页面再试');
                return;
            }
            _layer.confirm('确定要删除此角色吗？', function (confirmDialog) {
                _layer.close(confirmDialog);
                _loading.showLoading();
                _utils.delete('/api/role/del', {rid: rid}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert(result.msg || '未知错误');
                        return;
                    }
                    layer.msg('删除成功', {time: 1500}, function () {
                        window.location.reload(true);
                    });
                });
            });
        });

        // fetch data
        getRoleList({curr: 1});
    }

    layui.use(
        [
            'pageBuilder',
            'commonUtils',
            'commonLoading',
            'jquery',
            'layer',
            'dateFormat',
            'laypage',
            'form'
        ],
        roleListFunc
    );
} ());