import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"
import type {Links} from "@/lib/types.ts";

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


export function parseLinkHeader(header?: string): Links {
    if (!header) return {};
    const links: Links = {};

    for (const part of header.split(",")) {
        const section = part.split(";").map(s => s.trim());
        const url = section[0]?.replace(/^<|>$/g, "");
        const rel = section.find(s => s.startsWith("rel="))?.split("=")[1]?.replace(/"/g, "");

        if (url && rel) (links as any)[rel] = url;
    }
    return links;
}
