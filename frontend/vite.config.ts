/// <reference types="vitest/config" />

import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from "@tailwindcss/vite"
import { fileURLToPath, URL } from "node:url";



// https://vite.dev/config/
export default defineConfig({
    root: '.',
    plugins: [
        react(),
        tailwindcss(),
    ],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
        dedupe: ['react', 'react-dom'],
    },
    test: {
        globals: true,
        environment: 'jsdom',
        setupFiles: './src/__test__/setup/setup.ts',
        css: true,
    }
})
