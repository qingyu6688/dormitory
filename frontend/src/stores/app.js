import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
  state: () => ({
    // 侧边栏是否折叠
    sidebarCollapsed: false,
    // 标签页列表
    tabs: [],
    // 当前激活的标签页
    activeTab: ''
  }),

  actions: {
    // 切换侧边栏折叠状态
    toggleSidebar() {
      this.sidebarCollapsed = !this.sidebarCollapsed
    },

    // 添加标签页
    addTab(tab) {
      const exists = this.tabs.find(t => t.path === tab.path)
      if (!exists) {
        this.tabs.push(tab)
      }
      this.activeTab = tab.path
    },

    // 移除标签页
    removeTab(path) {
      const index = this.tabs.findIndex(t => t.path === path)
      if (index > -1) {
        this.tabs.splice(index, 1)
        // 如果关闭的是当前标签，切换到前一个或后一个
        if (this.activeTab === path && this.tabs.length > 0) {
          const nextTab = this.tabs[index] || this.tabs[index - 1]
          this.activeTab = nextTab.path
          return nextTab.path
        }
      }
      return null
    },

    // 关闭其他标签页
    closeOtherTabs(path) {
      this.tabs = this.tabs.filter(t => t.path === path)
      this.activeTab = path
    },

    // 关闭所有标签页
    closeAllTabs() {
      this.tabs = []
      this.activeTab = ''
    }
  }
})
