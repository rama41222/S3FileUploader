module.exports = {
  /*
  ** Headers of the page
  */
  head: {
    title: 'rumj-front',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'rumj file uploader' }
    ],
    link: [
      {rel: 'icon', type: 'image/x-icon', href: '/favicon.ico'},
      {rel: 'stylesheet', href: 'https://cdn.linearicons.com/free/1.0.0/icon-font.min.css'},
      {rel: 'stylesheet', href: 'https://use.fontawesome.com/releases/v5.0.8/css/all.css'},
      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=IBM+Plex+Serif:300,400,500,600'},
      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/css?family=GFS+Didot|Lato:100,300,400,700,900'},
      {rel: 'stylesheet', href: 'http://kenwheeler.github.io/slick/slick/slick.css'},
      {rel: 'stylesheet', href: 'http://kenwheeler.github.io/slick/slick/slick-theme.css'},
      {rel: 'stylesheet', href: 'https://fonts.googleapis.com/icon?family=Material+Icons'}
    ]
  },
 
  loading: { color: '#3B8070', height:'5px',failedColor:'#ff3726' },
  modules:[
    'bootstrap-vue/nuxt',
    '@nuxtjs/axios',
    '@nuxtjs/toast'
  ],
  axios: {
    baseURL: "your API"
  },
  toast: {
    position: 'top-right',
    duration: 2000,
  },
  plugins: [ { src:'~plugins/vee.js', ssr:false}, '~plugins/spinner.js', '~plugins/axios.js'],
  env: {
    contentType:'application/json',
    platform: 'web',
    version: '1.0',
    time: new Date().getTime()
  },
  build: {
    vendor: ['vee-validate'],
  
    extend (config, { isDev, isClient, isServer }) {
      
      if (isServer) {
        config.externals = [
          require('webpack-node-externals')({
            whitelist: [ /^epic-spinners/]
          })
        ]
      }
      
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }
  }
}
