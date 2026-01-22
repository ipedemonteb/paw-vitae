export function specialtyIdFromSelf(self: string): number | null {
    const m = self.match(/\/(\d+)(?:\/)?$/);
    return m ? Number(m[1]) : null;
}
export function coverageIdFromSelf(self: string): number | null {
    const m = self.match(/\/(\d+)(?:\/)?$/);
    return m ? Number(m[1]) : null;
}

export function userIdFromSelf(self: string | undefined): string | null {
    if (!self) return null;
    const m = self.match(/\/(\d+)\/?$/);
    return m ? String(m[1]) : null;
}

export function appointmentIdFromSelf(self: string): string | null {
    const m = self.match(/\/(\d+)\/?$/);
    return m ? String(m[1]) : null;
}