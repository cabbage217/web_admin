/**
 * Created by Caby on 5/24/17.
 */

layui.define(['layer'], function(exports) {
    var layer = !window.parent.layer ? layui.layer : window.parent.layer;
    // 显示加载框
    var _loadingIndex = false;
    function _showLoading() {
        if (!_loadingIndex) {
            _loadingIndex = layer.load();
        }
    }
    // 隐藏加载框
    function _hideLoading() {
        if (_loadingIndex) {
            layer.close(_loadingIndex);
            _loadingIndex = false;
        }
    }
    //对外开放接口
    var func =  {
        //显示加载框
        showLoading: _showLoading,
        //隐藏加载框
        hideLoading: _hideLoading
    };
    exports('commonLoading', func);
});