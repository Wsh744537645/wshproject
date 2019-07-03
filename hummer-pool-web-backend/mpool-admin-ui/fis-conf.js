fis.hook('commonjs')
fis.match('{js/config.js,views/**.js}', {
  useHash: true
});

fis.match('app/css/*/**.css', {
  useHash: true
});

fis.match('views/**.{html,css}',{
	useHash:true
})
