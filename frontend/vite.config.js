import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Proxy only /api calls and /logout to Spring Boot (port 9090)
      '/api': {
        target: 'http://127.0.0.1:9090',
        changeOrigin: true,
        secure: false,
      },
      // /login POST is sent via fetch() in LoginPage — needs proxying for form submit
      // BUT navigating to /login must stay as React route, so we only proxy POST
      '/logout': {
        target: 'http://127.0.0.1:9090',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})

