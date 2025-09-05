const proxyConfig = [{
    context: ['/'],
    target: 'http://localhost:8080',
    secure: false,
    changeOrigin: true,
    logLevel: 'debug'
}];

export default proxyConfig;
