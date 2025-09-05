const proxyConfig = [{
    context: [
      '^/(?!app).*',          // kein /app
      '^/(?!assets).*',       // kein /assets
      '^/(?!favicon\\.ico).*' // kein favicon.ico
    ],
    target: 'http://localhost:8080',
    changeOrigin: true,
    secure: false
}];

export default proxyConfig;
