const proxyConfig = [{
    context: ['^/(?!app).*'],
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false
}];

export default proxyConfig;
