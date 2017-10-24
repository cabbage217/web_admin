/**
 * Created by Caby on 6/3/17.
 */
;(function () {
    function navigationListFunc() {
        var _utils = layui.commonUtils, _loading = layui.commonLoading, $ = layui.jquery, _layer = layui.layer,
            _dateFormat = layui.dateFormat, _pager = layui.laypage, _form = layui.form;
        // fetch data
        var _nagvigationListContainer = $('#navigationListContainer');
        var _navigationList = {};
        function getNavigationList(obj, first) {
            if (first) return;
            _nagvigationListContainer.html('');
            var pageNo = obj.curr;
            _loading.showLoading();
            $.get('/api/navigation/list', {pageNo: pageNo - 1}, function (result) {
                _loading.hideLoading();
                $('.layui-table').show();
                var pageSize = 20, totalCount = 0;
                if (result.code == 0) {
                    pageNo = result.content.pageNo + 1;
                    pageSize = result.content.pageSize;
                    totalCount = result.content.navigationCount;
                    $.each(result.content.navigationList, function (idx, navigation) {
                        var tr = $('<tr class="navigation_info_row" data-nid="' + navigation.id + '"></tr>');
                        tr.append('<td>' + (idx + 1) + '</td>');
                        tr.append('<td>' + navigation.name + '</td>');
                        tr.append('<td>' + navigation.orderNumber + '</td>');
                        tr.append('<td>' + (navigation.status == 0 ? '正常' : '禁用') + '</td>');
                        tr.append('<td>' + _dateFormat(new Date(navigation.createTime), 'yyyy-mm-dd HH:MM:ss') + '</td>');

                        tr.append('<td><div class="layui-btn-group" data-nid="' + navigation.id + '">'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_edit_navigation" title="编辑">'
                            +       '<i class="layui-icon">&#xe642;</i>'
                            +   '</button>'
                            +   '<button class="layui-btn layui-btn-primary layui-btn-small btn_delete_navigation" title="删除">'
                            +       '<i class="layui-icon">&#xe640;</i>'
                            +   '</button>'
                            + '</div></td>');

                        _nagvigationListContainer.append(tr);
                        _navigationList[navigation.id] = navigation;
                    });
                } else {
                    _layer.msg(result.msg ? result.msg : '获取导航栏列表失败');
                }
                !first && setTimeout(function () {
                    _pager.render({elem: 'pagination', count: totalCount, limit: pageSize, curr: pageNo, jump: getNavigationList});
                }, 0);
            });
        }

        // add or edit navigation
        var _btnAddNavigation = $('#btn_add_navigation');
        var _addNavigationHtmlContainer = $('#add_navigation_html_container');
        var _dialogIdx = false;
        function showEditNavigationDialog(nid) {
            if (_dialogIdx !== false) return;
            nid = nid || 0;
            var navigationInfo = _navigationList[nid] ? _navigationList[nid] : false;
            _addNavigationHtmlContainer.find('input[name=nid]').attr('value', nid);
            _addNavigationHtmlContainer.find('input[name=name]').attr('value', navigationInfo ? navigationInfo.name : '');
            _addNavigationHtmlContainer.find('input[name=orderNumber]').attr('value', navigationInfo ? navigationInfo.orderNumber : '0');

            var checkbox = $('input:checkbox[name=status]').removeAttr('checked');
            if (!navigationInfo || navigationInfo.status == 0) {
                checkbox.attr('checked', 'checked');
            }
            _btnAddNavigation.addClass('layui-btn-disabled');
            _dialogIdx = _layer.open({
                type: 1,
                area: ['400px', '320px'],
                title: '添加接口',
                content: _addNavigationHtmlContainer.html(),
                cancel: closeAddNavigationDialog
            });
            _form.render();
        }
        // add navigation
        _btnAddNavigation.click(showEditNavigationDialog);
        // confirm to add navigation
        _form.on('submit(confirm)', function (data) {
            _loading.showLoading();
            var requestUrl = '/api/navigation/add';
            if (data.field.nid && data.field.nid > 0) {
                requestUrl = '/api/navigation/update';
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
        // close add navigation dialog
        function closeAddNavigationDialog() {
            if (_dialogIdx !== false) {
                _layer.close(_dialogIdx);
            }
            _btnAddNavigation.removeClass('layui-btn-disabled');
            _dialogIdx = false;
        }
        // edit navigation
        var _bodyEle = $('body');
        _bodyEle.on('click', '.btn_edit_navigation', function () {
            var nid = $(this).parent().attr('data-nid');
            if (!nid) {
                layer.msg('找不到导航栏的id，无法进行编辑，请刷新页面再试');
                return;
            }
            showEditNavigationDialog(nid);
        });
        // delete navigation
        _bodyEle.on('click', '.btn_delete_navigation', function () {
            var nid = $(this).parent().attr('data-nid');
            if (!nid) {
                layer.msg('找不到导航栏的id，无法进行删除，请刷新页面再试');
                return;
            }
            _layer.confirm('确定要删除此导航栏吗？', function (confirmDialog) {
                _layer.close(confirmDialog);
                _loading.showLoading();
                _utils.delete('/api/navigation/del', {nid: nid}, function (result) {
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

        // do fetch data
        getNavigationList({curr: 1});
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
        navigationListFunc
    );
} ());