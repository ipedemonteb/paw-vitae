export function specialtyIdFromSelf(self: string): number | null {
    const m = self.match(/\/(\d+)(?:\/)?$/);
    return m ? Number(m[1]) : null;
}
export function coverageIdFromSelf(self: string): number | null {
    const m = self.match(/\/(\d+)(?:\/)?$/);
    return m ? Number(m[1]) : null;
}

export function userIdFromImageUrl(url: string | undefined): string | undefined {
    if (!url) {
        return undefined;
    }
    const m = url.match(/\/(\d+)(?=\/[^/]*$)/);
    if (!m) {
        return undefined;
    }
    return String(m[1]);
}
export function userIdFromSelf(self: string | undefined): string | undefined {
    if (!self) return undefined;
    const m = self.match(/\/(\d+)\/?$/);
    return m ? String(m[1]) : undefined;
}

export function appointmentIdFromSelf(self: string): string | null {
    const m = self.match(/\/(\d+)\/?$/);
    return m ? String(m[1]) : null;
}

export function officeIdFromSelf(self: string): string | undefined {
    const m = self.match(/(\d+)(?!.*\d)/);
    return m ? String(m[1]) : undefined;
}

export function buildDoctorOfficesUrl(id: string) {
    return `/doctors/${id}/offices`
}