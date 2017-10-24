/**
 * Created by Caby on 6/3/17.
 */
;(function () {
    function interfaceListFunc() {
        var _utils = layui.commonUtils, _loading = layui.commonLoading, $ = layui.jquery, _layer = layui.layer,
            _dateFormat = layui.dateFormat, _pager = layui.laypage, _form = layui.form,
            _multipleCheckbox = layui.multipleCheckbox;
        // fetch data
        var _interfaceListContainer = $('#interfaceListContainer');
        var _interfaceList = {};
        function getInterfaceList(obj, first) {
            if (first) return;
            _interfaceListContainer.html('');
            _interfaceList = {};
            var pageNo = obj.curr;
            _loading.showLoading();
            $.get('/api/interface/list', {pageNo: pageNo - 1}, function (result) {
                _loading.hideLoading();
                $('.layui-table').show();
                var pageSize = 20, totalCount = 0;
                if (result.code == 0) {
                    pageNo = result.content.pageNo + 1;
                    pageSize = result.content.pageSize;
                    totalCount = result.content.interfaceCount;
                    $.each(result.content.interfaceList, function (idx, oneInterface) {
                        var tr = $('<tr class="interface_info_row" data-iid="' + oneInterface.id + '"></tr>');
                        tr.append('<td>' + (idx + 1) + '</td>');
                        tr.append('<td>' + oneInterface.name + '<sub class="navigation_interface_in" data-iid="' + oneInterface.id + '">'
                            + (!oneInterface.navigationName ? '' : oneInterface.navigationName) + '</sub>' + '</td>');
                        tr.append('<td>' + (oneInterface.type == 0 ? 'API' : '页面') + '</td>');
                        tr.append('<td>' + oneInterface.url + '</td>');
                        tr.append('<td>' + oneInterface.orderNumber + '</td>');
                        tr.append('<td>' + (oneInterface.status == 0 ? '正常' : '禁用') + '</td>');
                        tr.append('<td>' + _dateFormat(new Date(oneInterface.createTime), 'yyyy-mm-dd HH:MM:ss') + '</td>');
                        tr.append('<td><div class="layui-btn-group" data-iid="' + oneInterface.id + '">'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_edit_interface" title="编辑">'
                            +       '<i class="layui-icon">&#xe642;</i>'
                            +   '</button>'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_delete_interface" title="删除">'
                            +       '<i class="layui-icon">&#xe640;</i>'
                            +   '</button>'
                            + (function (isNav) {
                                var htmlStr = '';
                                if (isNav) {
                                    htmlStr +=  '<button class="layui-btn layui-btn-primary layui-btn-small btn_set_nav" title="设置导航栏">'
                                        +       '<i class="layui-icon">&#xe649;</i>'
                                        +   '</button>'
                                }
                                return htmlStr;
                            } (oneInterface.type == 1))
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_set_roles" title="设置角色">'
                            +       '<i class="layui-icon">&#xe612;</i>'
                            +   '</button>'
                            + '</div></td>');
                        _interfaceListContainer.append(tr);

                        _interfaceList[oneInterface.id] = oneInterface;
                    });
                } else {
                    _layer.msg(result.msg ? result.msg : '获取角色列表失败');
                }
                if (!first) {
                    setTimeout(function () {
                        _pager.render({elem: 'pagination', count: totalCount, limit: pageSize, curr: pageNo, jump: getInterfaceList});
                    }, 0);
                }
            });
        }
        getInterfaceList({curr: 1});

        // add or edit interface
        var _btnAddInterface = $('#btn_add_interface');
        var _addInterfaceHtmlContainer = $('#add_interface_html_container');
        var _dialogIdx = false;
        function showEditInterfaceDialog(iid) {
            if (_dialogIdx !== false) return;
            iid = iid || 0;
            var interfaceInfo = _interfaceList[iid] ? _interfaceList[iid] : false;
            _addInterfaceHtmlContainer.find('input[name=iid]').val(iid);
            _addInterfaceHtmlContainer.find('input[name=name]').attr('value', interfaceInfo ? interfaceInfo.name : '');
            _addInterfaceHtmlContainer.find('input[name=url]').attr('value', interfaceInfo ? interfaceInfo.url : '');
            var radios = $('input:radio[name=type]').removeAttr('checked');
            var checkbox = $('input:checkbox[name=status]').removeAttr('checked');
            if (!interfaceInfo) {
                radios.filter('[value=0]').attr('checked', "checked");
                checkbox.attr('checked', 'checked');
            } else {
                radios.filter('[value=' + interfaceInfo.type + ']').attr('checked', 'checked');
                if (interfaceInfo.status == 0) {
                    checkbox.attr('checked', 'checked');
                }
            }
            _addInterfaceHtmlContainer.find('input[name=orderNumber]').attr('value', interfaceInfo ? interfaceInfo.orderNumber : '0');

            _btnAddInterface.addClass('layui-btn-disabled');
            _dialogIdx = _layer.open({
                type: 1,
                area: ['400px', '420px'],
                title: '添加接口',
                content: _addInterfaceHtmlContainer.html(),
                cancel: closeAddInterfaceDialog});
            _form.render();
        }
        // add interface
        _btnAddInterface.click(showEditInterfaceDialog);
        // close dialog
        function closeAddInterfaceDialog() {
            if (_dialogIdx !== false) {
                _layer.close(_dialogIdx);
            }
            _btnAddInterface.removeClass('layui-btn-disabled');
            _dialogIdx = false;
        }
        // confirm to add interface
        _form.on('submit(confirm)', function (data) {
            _loading.showLoading();
            var requestUrl = '/api/interface/add';
            if (data.field.iid && data.field.iid > 0) {
                requestUrl = '/api/interface/update';
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

        var _bodyEle = $('body');
        // delete interface
        _bodyEle.on('click', '.btn_delete_interface', function () {
            var iid = $(this).parent().attr('data-iid');
            if (!iid) {
                layer.msg('找不到接口的id，无法进行删除，请刷新页面再试');
                return;
            }
            _layer.confirm('确定要删除此接口吗？', function (confirmDialog) {
                _layer.close(confirmDialog);
                _loading.showLoading();
                _utils.delete('/api/interface/del', {iid: iid}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert(result.msg || '未知错误');
                        return;
                    }
                    layer.msg('删除成功', {time: 1500}, function () {
                        if (_interfaceList[iid].type == 1) {
                            window.location.reload(true);
                        } else {
                            $('.interface_info_row[data-iid="' + iid + '"]').remove();
                            _interfaceList[iid] = undefined;
                        }
                    });
                });
            });
        });
        // edit interface
        _bodyEle.on('click', '.btn_edit_interface', function () {
            var iid = $(this).parent().attr('data-iid');
            if (!iid) {
                layer.msg('找不到接口的id，无法进行编辑，请刷新页面再试');
                return;
            }
            showEditInterfaceDialog(iid);
        });
        // set navigation
        var _navigationList = false;
        _bodyEle.on('click', '.btn_set_nav', function () {
            var iid = $(this).parent().attr('data-iid');
            if (!iid) {
                layer.msg('找不到接口的id，无法进行编辑，请刷新页面再试');
                return;
            }
            if (!_navigationList) {
                _loading.showLoading();
                _utils.get('/api/navigation/enabled', {}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert('获取导航栏数据失败，' + (result.msg || '请重试'));
                        return;
                    }
                    _navigationList = result.content;
                    showChangeNavigationDialog(iid);
                });
            } else {
                showChangeNavigationDialog(iid);
            }
        });
        // showChangeNavigationDialog
        var _changeNavigationHtmlContainer = $('#change_navigation_html_container');
        var _changeNavigationDialogIdx = false;
        function showChangeNavigationDialog(iid) {
            if (_changeNavigationDialogIdx !== false) return;
            var interfaceInfo = _interfaceList[iid];
            if (!interfaceInfo) {
                layer.msg('找不到接口的数据，无法进行操作，请刷新页面再试');
                return;
            }
            _changeNavigationHtmlContainer.find('input[name=iid]').attr('value', iid);
            var html = '';
            var hasSet = false;
            $.each(_navigationList, function (idx, navigation) {
                if (interfaceInfo.nid == navigation.id) {
                    html += '<option value="' + navigation.id + '" selected="selected">' + navigation.name + '</option>';
                    hasSet = true;
                } else {
                    html += '<option value="' + navigation.id + '">' + navigation.name + '</option>';
                }
            });
            html = '<option value="0"' + (!hasSet ? ' selected="selected"' : '') + '>不设置</option>' + html;
            _changeNavigationHtmlContainer.find('select[name=nid]').html(html);
            _changeNavigationDialogIdx = _layer.open({
                type: 1,
                area: ['400px', '350px'],
                title: '设置导航栏 -- ' + interfaceInfo.name,
                content: _changeNavigationHtmlContainer.html(),
                cancel: closeChangeNavigationDialog});
            _form.render();
        }
        // closeChangeNavigationDialog
        function closeChangeNavigationDialog() {
            if (_changeNavigationDialogIdx !== false) {
                _layer.close(_changeNavigationDialogIdx);
                _changeNavigationDialogIdx = false;
            }
        }
        // do change navigation
        _form.on('submit(confirm_changing_navigation)', function (data) {
            if (data.field.nid == _interfaceList[data.field.iid].nid) {
                _layer.msg('成功');
                closeChangeNavigationDialog();
                return false;
            }
            _loading.showLoading();
            _utils.put('/api/interface/setNav', data.field, function (result) {
                _loading.hideLoading();
                if (result.code != 0) {
                    _layer.alert(result.msg || '未知错误，请重试');
                    return;
                }
                _layer.msg('设置导航栏成功', {time: 1500}, function () {
                    window.location.reload(true);
                });
            });
            return false;
        });
        // set roles
        var _roleList = false;
        var _changeRolesDialogIdx = false;
        _bodyEle.on('click', '.btn_set_roles', function () {
            if (_changeRolesDialogIdx) return;
            var iid = $(this).parent().attr('data-iid');
            if (!iid) {
                layer.msg('找不到接口的id，无法进行操作，请刷新页面再试');
                return;
            }
            checkRoleList(iid);
        });
        // check _roleList
        function checkRoleList(iid) {
            if (!_roleList) {
                _utils.get('/api/role/all', {}, function (result) {
                    _loading.hideLoading();
                    if (result.code != 0) {
                        _layer.alert('获取角色列表失败，' + (result.msg || '请重试'));
                        return;
                    }
                    _roleList = result.content;
                    showChangeRolesDialog(iid);
                });
            } else {
                showChangeRolesDialog(iid);
            }
        }
        // show select roles dialog
        var _changeRolesHtmlContainer = $('#change_roles_html_container');
        var _roleListContainer = $('#roleListContainer');
        var _btnChangeRolesConfirm = $('#btn_change_roles_confirm');
        var _interfaceCurrentRids = {};
        function showChangeRolesDialog(iid) {
            _loading.hideLoading();
            _roleListContainer.html('');
            var needFind = typeof _interfaceCurrentRids[iid] == 'undefined';
            if (needFind) {
                _interfaceCurrentRids[iid] = [];
            }
            var allRids = [];
            var interfaceInfo = _interfaceList[iid];
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
                    if (role.interfaceList && role.interfaceList.length > 0) {
                        $.each(role.interfaceList, function (idxInterface, oneInterface) {
                            if (oneInterface.id == interfaceInfo.id) {
                                _interfaceCurrentRids[iid].push(role.id);
                                return false;
                            }
                        });
                    }
                }
            });
            _btnChangeRolesConfirm.attr('data-iid', iid);
            _changeRolesDialogIdx = _layer.open({
                    type: 1,
                    area: ['860px', '500px'],
                    title: '设置角色 -- ' + interfaceInfo.name,
                    content: _changeRolesHtmlContainer.html(),
                    cancel: closeChangeRolesDialog}
            );
            _multipleCheckbox.reset(allRids, [].concat(_interfaceCurrentRids[iid]));
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
            var iid = $(this).attr('data-iid');
            if (_utils.isArrayEqual(_interfaceCurrentRids[iid], rolesSelected)) {
                closeChangeRolesDialog();
                _layer.msg('成功', {time: 1500});
                return;
            }
            _loading.showLoading();
            _utils.put('/api/interface/setRoles', {iid: iid, roleIds: rolesSelected}, function (result) {
                _loading.hideLoading();
                if (result.code != 0) {
                    _layer.alert('失败，' + (result.msg || '请重试'));
                    return;
                }
                _interfaceCurrentRids[iid] = rolesSelected;
                closeChangeRolesDialog();
                if (_interfaceList[iid].type == 1) {
                    _layer.msg('成功', {time: 1500}, function () {
                        window.location.reload(true);
                    });
                } else {
                    _layer.msg('成功', {time: 1500});
                }
            });
        });
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
            'form',
            'multipleCheckbox'
        ],
        interfaceListFunc
    );
} ());