/**
 * Created by Caby on 3/24/17.
 */
layui.define(['jquery','form'], function(exports) {
    var $ = layui.jquery, form = layui.form;
    var _allIds = [], _checkedIds = [];
    var _options = {
        'checkbox_filter': 'checkMe',
        'checkbox_all_filter': 'checkAll'
    };
    function _refreshCheckboxStatus() {
        var needRender = false;
        var checkboxes = $('input[lay-filter="' + _options['checkbox_filter'] + '"]');
        if (checkboxes && 0 < checkboxes.length) {
            $.each(checkboxes, function(idx, val) { $(val).prop('checked', -1 < _checkedIds.indexOf(parseInt(val.value))); });
            needRender = true;
        }
        var checkAll = $('input[lay-filter="' + _options['checkbox_all_filter'] + '"]');
        if (checkAll && 0 < checkAll.length) {
            checkAll.prop('checked', 0 < _checkedIds.length && _allIds.length == _checkedIds.length);
            needRender = true;
        }
        needRender && form.render('checkbox');
    }
    // 配置
    function _config(options) {
        options = options || {};
        _options['checkbox_filter'] = options['checkbox_filter'] || 'checkbox(checkMe)';
        _options['checkbox_all_filter'] = options['checkbox_all_filter'] || 'checkbox(checkAll)';
    }
    // 重置
    function _reset(allIds, checkedIds, options) {
        if (options) { _config(options) }
        _allIds = [].concat(allIds);
        _checkedIds = [].concat(checkedIds);
        _refreshCheckboxStatus();
    }
    // 设置选中项ids
    function _checkIds(ids) {
        ids = [].concat(ids);
        var needRender = false;
        $.each(ids, function(idx, val) {
            var id = parseInt(val);
            if (-1 == _checkedIds.indexOf(id)) {
                _checkedIds.push(id);
                needRender = true;
            }
        });
        needRender && _refreshCheckboxStatus();
    }
    function _checkAll() {
        _checkIds(_allIds);
    }
    // 设置不选中项ids
    function _uncheckIds(ids) {
        ids = [].concat(ids);
        var needRender = false;
        $.each(ids, function(idx, val) {
            var idxInCheckedIds =_checkedIds.indexOf(parseInt(val));
            if (-1 != idxInCheckedIds) {
                _checkedIds.splice(idxInCheckedIds, 1);
                needRender = true;
            }
        });
        needRender && _refreshCheckboxStatus();
    }
    function _uncheckAll() {
        _uncheckIds(_allIds)
    }
    // 重置所有待选ids
    function _resetAllIds(ids) {
        _allIds = [].concat(ids);
        _checkedIds = [];
        _refreshCheckboxStatus();
    }
    // 获取已选中ids
    function _getCheckedIds() {
        return _checkedIds.concat();
    }
    // 获取未选中的ids
    function _getUncheckedIds() {
        var ids = [];
        $.each(_allIds, function(idx, val) { if (-1 == _checkedIds.indexOf(val)) { ids.push(val); } });
        return ids;
    }
    // 获取所有ids
    function _getAllIds() {
        return _allIds.concat();
    }
    //监听选择项checkbox
    form.on('checkbox(' + _options['checkbox_filter'] + ')', function(data) {
        var id = parseInt(data.value);
        data.elem.checked ? _checkIds([id]) : _uncheckIds([id]);
    });
    //监听全选checkbox
    form.on('checkbox(' + _options['checkbox_all_filter'] + ')', function(data) {
        data.elem.checked ? _checkAll() : _uncheckAll();
    });
    //对外开放接口
    var func =  {
        // 配置
        config: _config,
        // 初始化/重置
        reset: _reset,
        // 设置选中项ids
        checkIds: _checkIds,
        // 选中所有
        checkAll: _checkAll,
        // 设置不选中项ids
        uncheckIds: _uncheckIds,
        // 不选中所有
        uncheckAll: _uncheckAll,
        // 重置所有待选ids
        resetAllIds: _resetAllIds,
        // 获取已选中ids
        getCheckedIds: _getCheckedIds,
        // 获取未选中的ids
        getUncheckedIds: _getUncheckedIds,
        // 获取所有ids
        getAllIds: _getAllIds,
        //获取所有ids、已选中ids和未选中ids
        getAllStatus: function() {
            return {'all': _getAllIds(), 'checked': _getCheckedIds(), 'unchecked': _getUncheckedIds()}
        }
    };
    exports('multipleCheckbox', func);
});