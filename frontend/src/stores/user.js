import { defineStore } from 'pinia'
import { login, register, getUserInfo, logout } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: null,
    menus: []
  }),

  actions: {
    // 登录
    async login(loginData) {
      try {
        const res = await login(loginData)
        this.token = res.data.token
        this.userInfo = res.data.user
        this.menus = res.data.menus || []
        localStorage.setItem('token', res.data.token)
        ElMessage.success('登录成功')
        return res
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 注册
    async register(registerData) {
      try {
        const res = await register(registerData)
        ElMessage.success('注册成功')
        return res
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 获取用户信息
    async fetchUserInfo() {
      try {
        const res = await getUserInfo()
        this.userInfo = res.data.user
        this.menus = res.data.menus || []
        return res
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 登出
    async logout() {
      try {
        await logout()
        this.token = ''
        this.userInfo = null
        this.menus = []
        localStorage.removeItem('token')
        ElMessage.success('退出成功')
      } catch (error) {
        return Promise.reject(error)
      }
    },

    // 更新头像
    updateAvatar(avatar) {
      if (this.userInfo) {
        this.userInfo.avatar = avatar
      }
    }
  }
})
