# cloud

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

app.vue做顶级出口，只放router-views
user包括全部用户面板信息
buy包括全部购买流程页面
components是三个公共头部和公共底部
views是首页强制显示的内容
    //about组件是首页顶级出口
    //home组件是首页内容区域显示
