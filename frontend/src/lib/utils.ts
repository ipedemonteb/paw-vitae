import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function extractIdFromUrl(url: string): string {
  const segments = url.replace(/\/$/, "").split("/")
  return segments.pop() || ""
}
