import {
  CircleCheckIcon,
  InfoIcon,
  Loader2Icon,
  OctagonXIcon,
  TriangleAlertIcon,
} from "lucide-react"
import { useTheme } from "next-themes"
import { Toaster as Sonner, type ToasterProps } from "sonner"

const Toaster = ({ ...props }: ToasterProps) => {
  const { theme = "system" } = useTheme()

  return (
    <Sonner
      closeButton
      richColors
      theme={theme as ToasterProps["theme"]}
      className="toaster group"
      icons={{
        success: <CircleCheckIcon className="size-5" />,
        info: <InfoIcon className="size-5" />,
        warning: <TriangleAlertIcon className="size-5" />,
        error: <OctagonXIcon className="size-5" />,
        loading: <Loader2Icon className="size-5 animate-spin" />,
      }}
      style={
        {
            "--normal-bg": "var(--popover)",
            "--normal-text": "var(--popover-foreground)",
            "--normal-border": "var(--border)",
            "--border-radius": "var(--radius)",

            "--success-bg": "var(--success-lighter)",
            "--success-text": "var(--success)",
            "--success-border": "var(--success)",

            "--error-bg": "var(--danger-light)",
            "--error-text": "var(--danger)",
            "--error-border": "var(--danger)",

            "--warning-bg": "var(--warning-lighter)",
            "--warning-text": "var(--warning)",
            "--warning-border": "var(--warning)",
        } as React.CSSProperties
      }
      {...props}
    />
  )
}

export { Toaster }
