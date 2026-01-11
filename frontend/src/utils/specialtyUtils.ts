export function specialtyIdFromSelf(self: string): number | null {
    const m = self.match(/\/(\d+)(?:\/)?$/);
    return m ? Number(m[1]) : null;
}