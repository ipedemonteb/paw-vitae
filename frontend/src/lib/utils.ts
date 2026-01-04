import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function extractIdFromUrl(url: string): string {
  const segments = url.replace(/\/$/, "").split("/")
  return segments.pop() || ""
}

export function clampRating(rating: number) {
    if (Number.isNaN(rating)) return 0;
    return Math.max(0, Math.min(5, Math.round(rating)));
}
