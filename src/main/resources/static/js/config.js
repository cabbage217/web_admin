/**
 * Created by Caby on 5/24/17.
 */

layui.config({
    base: '/js/'
}).extend({
    pageBuilder: 'pageBuilder',
    md5: 'md5',
    sha1: 'sha1',
    commonLoading: 'loading',
    commonUtils: 'utils',
    dateFormat: 'dateFormat',
    multipleCheckbox: 'multipleCheckbox'
});

layui.use(['layer'], function () {
    var _layer = layui.layer;
    _layer.config({
        offset: '150px'
    });
});