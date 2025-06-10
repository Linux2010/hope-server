export function triggerWindowResizeEvent () {
  let event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}
export const groupBy = (list, fn) => {
  const groups = {}
  list.forEach(function (o) {
    const group = fn(o)
    groups[group] = groups[group] || []
    groups[group].push(o)
  })
  return groups
}
