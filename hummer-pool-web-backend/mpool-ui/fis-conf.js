fis.hook('commonjs')

//npm i -g babel-core fis3-parser-translate-es6
fis.match('{js/config.js,views/**.js,js/app.js,js/configURL.js}', {
    useHash: true
});

fis.match('app/css/*/**.css', {
    useHash: true
});

fis.match('views/**.{html,css}', {
    useHash: true
})

fis.match('*.js', {
    optimizer: fis.plugin('uglify-js')
});
fis.match('{js/config.js,app/homepage/**.js}', {
    optimizer: null
});

fis.match('*.css', {
    // fis-optimizer-clean-css 插件进行压缩，已内置
    optimizer: fis.plugin('clean-css')
});

fis.match('*.png', {
    // fis-optimizer-png-compressor 插件进行压缩，已内置
    optimizer: fis.plugin('png-compressor')
});

//