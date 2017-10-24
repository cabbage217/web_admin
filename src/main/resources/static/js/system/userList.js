/**
 * Created by Caby on 6/3/17.
 */
;(function () {
    function userListFunc() {
        var _utils = layui.commonUtils, _loading = layui.commonLoading, $ = layui.jquery, _layer = layui.layer,
            _dateFormat = layui.dateFormat, _pager = layui.laypage, _form = layui.form, _md5 = layui.md5,
            _sha1 = layui.sha1, _multipleCheckbox = layui.multipleCheckbox;
        // fetch data
        var _userListContainer = $('#userListContainer');
        var _userList = {};
        function getUserList(obj, first) {
            if (first) return;
            var pageNo = obj.curr;
            _userListContainer.html('');
            _loading.showLoading();
            $.get('/api/user/list', {pageNo: pageNo - 1}, function (result) {
                _loading.hideLoading();
                $('.layui-table').show();
                var pageSize = 20, totalCount = 0;
                if (result.code == 0) {
                    pageNo = result.content.pageNo + 1;
                    pageSize = result.content.pageSize;
                    totalCount = result.content.userCount;
                    var userList = result.content.userList;
                    $.each(userList, function (idx, user) {
                        var tr = $('<tr class="user_info_row" data-nid="' + user.id + '"></tr>');
                        tr.append('<td>' + (idx + 1) + '</td>');
                        tr.append('<td>' + user.username + '</td>');
                        tr.append('<td class="user_role_cell">' + (function (roles) {
                                var roleNames = '';
                                $.each(roles, function (index, role) {
                                    roleNames += (index != 0 ? '，' : '') + role.name;
                                });
                                return roleNames;
                            } (user.roles)) + '</td>');
                        tr.append('<td>' + (user.status == 0 ? '正常' : '禁用') + '</td>');
                        tr.append('<td>' + _dateFormat(new Date(user.createTime), 'yyyy-mm-dd HH:MM:ss') + '</td>');
                        tr.append('<td><div class="layui-btn-group" data-uid="' + user.id + '">'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_edit_user" title="编辑">'
                            +       '<i class="layui-icon">&#xe642;</i>'
                            +   '</button>'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_delete_user" title="删除">'
                            +       '<i class="layui-icon">&#xe640;</i>'
                            +   '</button>'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_set_roles" title="设置角色">'
                            +       '<i class="layui-icon">&#xe612;</i>'
                            +   '</button>'
                            + '</div></td>');
                        _userListContainer.append(tr);
                        _userList[user.id] = user;
                    });
                } else {
                    _layer.msg(result.msg ? result.msg : '获取用户列表失败');
                }
                if (!first) {
                    setTimeout(function () {
                        _pager.render({elem: 'pagination', count: totalCount, limit: pageSize, curr: pageNo, jump: getUserList});
                    }, 0);
                }
            });
        }
        // add or edit user
        var _btnAddUser = $('#btn_add_user');
        var _addUserHtmlContainer = $('#add_user_html_container');
        var _dialogIdx = false;
        function showEditUserDialog(uid) {
            if (_dialogIdx !== false) return;
            uid = uid || 0;
            var userInfo = _userList[uid] ? _userList[uid] : false;
            _addUserHtmlContainer.find('input[name=uid]').attr('value', uid);
            _addUserHtmlContainer.find('input[name=username]').attr('value', userInfo ? userInfo.username : '');
            var statusCheckbox = _addUserHtmlContainer.find('input:checkbox[name=status]').removeAttr('checked');
            var nameInput = _addUserHtmlContainer.find('input[name=pwd]').removeAttr('lay-verify');
            if (!userInfo) {
                nameInput.attr('value', '123456');
                nameInput.attr('lay-verify', 'required');
                statusCheckbox.attr('checked', 'checked');
            } else {
                nameInput.attr('value', '');
                nameInput.attr('placeholder', '留空则不改变密码');
                if (userInfo.status == 0) {
                    statusCheckbox.attr('checked', 'checked');
                }
            }

            _btnAddUser.addClass('layui-btn-disabled');
            _dialogIdx = _layer.open({
                type: 1,
                area: ['400px', '320px'],
                title: '添加接口',
                content: _addUserHtmlContainer.html(),
                cancel: closeAddUserDialog
            });
            _form.render();
        }
        // add user
        _btnAddUser.click(showEditUserDialog);
        // confirm to add user
        _form.on('submit(confirm)', function (data) {
            _loading.showLoading();
            var requestUrl = '/api/user/add';
            if (data.field.uid && data.field.uid > 0) {
                requestUrl = '/api/user/update';
            }
            if (typeof data.field.status == 'undefined') {
                data.field.status = 1;
            }
            if (data.field.pwd) {
                data.field.pwd = _sha1(_md5(data.field.pwd) + _utils.getPasswordSalt());
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
        // close add user dialog
        function closeAddUserDialog() {
            if (_dialogIdx !== false) {
                _layer.close(_dialogIdx);
            }
            _btnAddUser.removeClass('layui-btn-disabled');
            _dialogIdx = false;
        }
        // edit user
        var _bodyEle = $('body');
        _bodyEle.on('click', '.btn_edit_user', function () {
            var uid = $(this).parent().attr('data-uid');
            if (!uid) {
                layer.msg('找不到用户的id，无法进行编辑，请刷新页面再试');
                return;
            }
            showEditUserDialog(uid);
        });
        // delete user
        _bodyEle.on('click', '.btn_delete_user', function () {
            var uid = $(this).parent().attr('data-uid');
            if (!uid) {
                layer.msg('找不到用户的id，无法进行删除，请刷新页面再试');
                return;
            }
            _layer.confirm('确定要删除此用户吗？', function (confirmDialog) {
                _layer.close(confirmDialog);
                _loading.showLoading();
                _utils.delete('/api/user/del', {uid: uid}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert(result.msg || '未知错误');
                        return;
                    }
                    $('.user_info_row[data-nid="' + uid + '"]').remove();
                });
            });
        });
        // set roles
        var _roleList = false;
        var _changeRolesDialogIdx = false;
        _bodyEle.on('click', '.btn_set_roles', function () {
            if (_changeRolesDialogIdx) return;
            var uid = $(this).parent().attr('data-uid');
            if (!uid) {
                layer.msg('找不到接口的id，无法进行操作，请刷新页面再试');
                return;
            }
            checkRoleList(uid);
        });
        // check _roleList
        function checkRoleList(uid) {
            if (!_roleList) {
                _utils.get('/api/role/all', {}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert('获取角色列表失败，' + (result.msg || '请重试'));
                        return;
                    }
                    _roleList = result.content;
                    showChangeRolesDialog(uid);
                });
            } else {
                showChangeRolesDialog(uid);
            }
        }
        // show select roles dialog
        var _changeRolesHtmlContainer = $('#change_roles_html_container');
        var _roleListContainer = $('#roleListContainer');
        var _btnChangeRolesConfirm = $('#btn_change_roles_confirm');
        var _userCurrentRids = {};
        function showChangeRolesDialog(uid) {
            _loading.hideLoading();
            _roleListContainer.html('');
            var needFind = typeof _userCurrentRids[uid] == 'undefined';
            if (needFind) {
                _userCurrentRids[uid] = [];
            }
            var allRids = [];
            var userInfo = _userList[uid];
            $.each(_roleList, function (idx, role) {
                var tr = $('<tr></tr>');
                tr.append('<td>' + (idx + 1) + '</td>');
                tr.append('<td>' + role.name + '</td>');
                tr.append('<td>' + role.des + '</td>');
                tr.append('<td>' + (role.status == 0 ? '正常' : '禁用') + '</td>');
                tr.append('<td><div class="layui-inline">'
                    + '<input type="checkbox" lay-skin="primary" lay-filter="checkMe" value="' + role.id + '">'
                    + '</div></td>');
                _roleListContainer.append(tr);

                allRids.push(role.id);
                if (needFind) {
                    $.each(userInfo.roles, function (idxUserRole, userRole) {
                        if (userRole.id == role.id) {
                            _userCurrentRids[uid].push(role.id);
                            return false;
                        }
                    });
                }
            });
            _btnChangeRolesConfirm.attr('data-uid', uid);
            _changeRolesDialogIdx = _layer.open({
                    type: 1,
                    area: ['860px', '500px'],
                    title: '设置角色 -- ' + userInfo.username,
                    content: _changeRolesHtmlContainer.html(),
                    cancel: closeChangeRolesDialog}
            );
            _multipleCheckbox.reset(allRids, [].concat(_userCurrentRids[uid]));
        }
        // close change roles dialog
        function closeChangeRolesDialog() {
            if (_changeRolesDialogIdx) {
                _layer.close(_changeRolesDialogIdx);
                _changeRolesDialogIdx = false;
            }
        }
        // cancel setting roles
        _bodyEle.on('click', '#btn_change_roles_cancel', closeChangeRolesDialog);
        // confirm to set roles
        _bodyEle.on('click', '#btn_change_roles_confirm', function () {
            var rolesSelected = _multipleCheckbox.getCheckedIds();
            var uid = $(this).attr('data-uid');
            if (_utils.isArrayEqual(_userCurrentRids[uid], rolesSelected)) {
                closeChangeRolesDialog();
                _layer.msg('成功', {time: 1500});
                return;
            }
            _loading.showLoading();
            _utils.put('/api/user/setRoles', {uid: uid, roleIds: rolesSelected}, function (result) {
                _loading.hideLoading();
                if (result.code != 0) {
                    _layer.alert('失败，' + (result.msg || '请重试'));
                    return;
                }
                _userCurrentRids[uid] = rolesSelected;
                closeChangeRolesDialog();
                var roleNames = [];
                $.each(_userCurrentRids[uid], function (idx, rid) {
                    $.each(_roleList, function (idxRole, oneRole) {
                        if (rid == oneRole.id) {
                            roleNames.push(oneRole.name);
                            return false;
                        }
                    });
                });
                $('.user_info_row[data-nid="' + uid + '"]').find('.user_role_cell').text(roleNames.join('，'));
            });
        });

        // get first page
        getUserList({curr: 1});
    }

    layui.use(
        [
            'laypage',
            'pageBuilder',
            'commonUtils',
            'commonLoading',
            'jquery',
            'layer',
            'dateFormat',
            'form',
            'md5',
            'sha1',
            'multipleCheckbox'
        ],
        userListFunc
    );
} ());