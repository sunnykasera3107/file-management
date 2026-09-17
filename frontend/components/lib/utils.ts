import { clsx, type ClassValue } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function slugToText(slug: string) {
  if (!slug) return '';
  
  return slug
    .replace(/[-_]+/g, ' ')               // Replace hyphens and underscores with a space
    .trim()                               // Remove trailing/leading spaces
    .replace(/\b[a-z]/g, (match) => {     // Capitalize the first letter of each word
      return match.toUpperCase();
  });
}

export function textToSlug(text: string) {
  if (!text) return '';
  return text
    .toString()
    .toLowerCase()
    .trim()
    .normalize('NFD')                  // Decompose combined graphemes (removes accents)
    .replace(/[\u0300-\u036f]/g, '')   // Remove diacritics/accents
    .replace(/[^a-z0-9\s-]/g, '')      // Remove all non-alphanumeric characters except spaces and hyphens
    .replace(/[\s_]+/g, '-')           // Replace spaces and underscores with a single hyphen
    .replace(/-+/g, '-');              // Remove consecutive hyphens
}

export function average(sum: number, count: number) {
  if (!sum || !count) return ""
  return (sum / count).toFixed(0);
}