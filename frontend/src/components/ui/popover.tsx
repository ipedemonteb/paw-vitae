import * as React from "react"
import * as PopoverPrimitive from "@radix-ui/react-popover"
import { RemoveScroll } from "react-remove-scroll"

import { cn } from "@/lib/utils"

type PopoverCtxValue = {
  open: boolean
  setOpen: (next: boolean) => void
}

const PopoverCtx = React.createContext<PopoverCtxValue | null>(null)

function usePopoverCtx() {
  const ctx = React.useContext(PopoverCtx)
  if (!ctx) throw new Error("PopoverContent must be used within <Popover />")
  return ctx
}

type PopoverProps = React.ComponentProps<typeof PopoverPrimitive.Root>

function Popover({
                   open: openProp,
                   defaultOpen,
                   onOpenChange,
                   ...props
                 }: PopoverProps) {
  const [uncontrolledOpen, setUncontrolledOpen] = React.useState(!!defaultOpen)
  const isControlled = openProp !== undefined
  const open = isControlled ? !!openProp : uncontrolledOpen

  const setOpen = React.useCallback(
      (next: boolean) => {
        if (!isControlled) setUncontrolledOpen(next)
        onOpenChange?.(next)
      },
      [isControlled, onOpenChange]
  )

  return (
      <PopoverCtx.Provider value={{ open, setOpen }}>
        <PopoverPrimitive.Root
            data-slot="popover"
            open={open}
            onOpenChange={setOpen}
            {...props}
        />
      </PopoverCtx.Provider>
  )
}

function PopoverTrigger({
                          ...props
                        }: React.ComponentProps<typeof PopoverPrimitive.Trigger>) {
  return <PopoverPrimitive.Trigger data-slot="popover-trigger" {...props} />
}

function PopoverContent({
                          className,
                          align = "center",
                          sideOffset = 4,
                          ...props
                        }: React.ComponentProps<typeof PopoverPrimitive.Content>) {
  const { open, setOpen } = usePopoverCtx()
  const contentRef = React.useRef<HTMLDivElement | null>(null)

  React.useEffect(() => {
    if (!open) return

    const onScrollCapture = (e: Event) => {
      const target = e.target as Node | null
      if (target && contentRef.current?.contains(target)) return
      setOpen(false)
    }

    const onResize = () => setOpen(false)

    window.addEventListener("scroll", onScrollCapture, true)
    window.addEventListener("resize", onResize)

    return () => {
      window.removeEventListener("scroll", onScrollCapture, true)
      window.removeEventListener("resize", onResize)
    }
  }, [open, setOpen])

  const onPointerDownCapture = (e: React.PointerEvent<HTMLDivElement>) => {
    const el = contentRef.current
    if (!el) return

    const scrollbarW = el.offsetWidth - el.clientWidth
    const scrollbarH = el.offsetHeight - el.clientHeight

    if (scrollbarW <= 0 && scrollbarH <= 0) return

    const r = el.getBoundingClientRect()
    const x = e.clientX
    const y = e.clientY

    const inVerticalScrollbar =
        scrollbarW > 0 && x >= r.right - scrollbarW && x <= r.right && y >= r.top && y <= r.bottom

    const inHorizontalScrollbar =
        scrollbarH > 0 && y >= r.bottom - scrollbarH && y <= r.bottom && x >= r.left && x <= r.right

    if (inVerticalScrollbar || inHorizontalScrollbar) {
      setOpen(false)
    }
  }

  return (
      <PopoverPrimitive.Portal>
        <RemoveScroll allowPinchZoom shards={[contentRef]}>
          <PopoverPrimitive.Content
              ref={contentRef}
              data-slot="popover-content"
              align={align}
              sideOffset={sideOffset}
              onPointerDownCapture={onPointerDownCapture}
              className={cn(
                  "bg-popover text-popover-foreground data-[state=open]:animate-in data-[state=closed]:animate-out data-[state=closed]:fade-out-0 data-[state=open]:fade-in-0 data-[state=closed]:zoom-out-95 data-[state=open]:zoom-in-95 data-[side=bottom]:slide-in-from-top-2 data-[side=left]:slide-in-from-right-2 data-[side=right]:slide-in-from-left-2 data-[side=top]:slide-in-from-bottom-2 z-50 w-72 origin-(--radix-popover-content-transform-origin) rounded-md border p-4 shadow-md outline-hidden",
                  className
              )}
              {...props}
          />
        </RemoveScroll>
      </PopoverPrimitive.Portal>
  )
}

function PopoverAnchor({
                         ...props
                       }: React.ComponentProps<typeof PopoverPrimitive.Anchor>) {
  return <PopoverPrimitive.Anchor data-slot="popover-anchor" {...props} />
}

export { Popover, PopoverTrigger, PopoverContent, PopoverAnchor }
